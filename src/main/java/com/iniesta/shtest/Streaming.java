package com.iniesta.shtest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import ch.hsr.geohash.GeoHash;

public class Streaming {

	private static final String TMP_INPUT = "/tmp/input";

	public static void main(String[] args) throws IOException {
		// Create table for HBase querying data
//		Configuration hConf = HBaseConfiguration.create();
//		Connection connection = ConnectionFactory.createConnection(hConf);
//		Table table = connection.getTable(TableName.valueOf("deals"));

		SparkConf config = new SparkConf().setMaster("local[2]").setAppName("Sample Streaming");

		JavaStreamingContext jssc = new JavaStreamingContext(config, Durations.seconds(5));

		File inputDir = new File(TMP_INPUT);
		if (!inputDir.exists()) {
			inputDir.mkdir();
		}

		JavaDStream<String> stream = jssc.textFileStream(TMP_INPUT);

		JavaDStream<String> streamBase32 = stream.map(new ExtractBase32());

		streamBase32.print();

		streamBase32.map(new QueryHBase()).print();

		jssc.start();

		jssc.awaitTermination();

		jssc.close();
//		connection.close();
	}

	public static class ExtractBase32 implements Function<String, String> {
		private static final long serialVersionUID = 7514579317126511264L;

		@Override
		public String call(String input) throws Exception {
			String[] fields = input.split(";");
			double latitude = new BigDecimal(fields[0]).doubleValue();
			double longitude = new BigDecimal(fields[1]).doubleValue();
			GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 6);
			return geoHash.toBase32();
		}
	}

	public static class QueryHBase implements Function<String, Deal> {

		private static final long serialVersionUID = 6718235695603890906L;
		private transient Table table;
		private transient Connection connection;

		private void init() {
			try {
				Configuration hConf = HBaseConfiguration.create();
				connection = ConnectionFactory.createConnection(hConf);
				table = connection.getTable(TableName.valueOf("deals"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Deal call(String inputGeohashBase32) throws Exception {
			init();
			Deal deal = null;
			Get get = new Get(Bytes.toBytes(inputGeohashBase32));

			Result result = table.get(get);

			if (result != null) {
				int id = Bytes.toInt(result.getValue(Bytes.toBytes("details"), Bytes.toBytes("id")));
				long tsS = Bytes.toLong(result.getValue(Bytes.toBytes("details"), Bytes.toBytes("ts_start")));
				long tsE = Bytes.toLong(result.getValue(Bytes.toBytes("details"), Bytes.toBytes("ts_end")));
				deal = new Deal();
				deal.setId(id);
				deal.setTsStart(tsS);
				deal.setTsEnd(tsE);
			}
			closeConnection();
			return deal;
		}

		private void closeConnection() {
			if(connection!=null){
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
