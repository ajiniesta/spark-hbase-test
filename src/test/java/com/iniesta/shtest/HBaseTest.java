package com.iniesta.shtest;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * Unit test for simple App.
 */
public class HBaseTest {

	private static final String DEALS_TABLE = "deals";

	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
		
		try(Connection connection = ConnectionFactory.createConnection(conf)){
			Admin admin = connection.getAdmin();
			if(!admin.tableExists(TableName.valueOf(DEALS_TABLE))){
				System.out.println("Table doesn't exists.... let's create it");
				HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(DEALS_TABLE));
				HColumnDescriptor columnDescriptor = new HColumnDescriptor(Bytes.toBytes("details"));
				tableDescriptor.addFamily(columnDescriptor);
				admin.createTable(tableDescriptor);
			} else {
				System.out.println("Table already exists");
			}
			Table table = connection.getTable(TableName.valueOf(DEALS_TABLE));
			Put put = new Put(Bytes.toBytes("ezjmuq"));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("id"), Bytes.toBytes(1));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("ts_start"), Bytes.toBytes(System.currentTimeMillis()));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("ts_end"), Bytes.toBytes(System.currentTimeMillis()+3600));
			table.put(put);
			put = new Put(Bytes.toBytes("ezjmuw"));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("id"), Bytes.toBytes(2));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("ts_start"), Bytes.toBytes(System.currentTimeMillis()));
			put.addColumn(Bytes.toBytes("details"), Bytes.toBytes("ts_end"), Bytes.toBytes(System.currentTimeMillis()+3600));
			table.put(put);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
