package com.iniesta.shtest;

import org.junit.Test;

import ch.hsr.geohash.GeoHash;

public class GeohashingTest {

	@Test
	public void test1(){
		testing(12);
		testing(11);
		testing(6);
	}

	private void testing(int precision) {
		System.out.println("Precision " + precision);
		GeoHash geohash;		
		long start, time;
		String old = "";
		boolean allTheSame = true;
		System.out.println("O'donnell with Doctor Esquerdo");
		start = System.currentTimeMillis();
		
		geohash = GeoHash.withCharacterPrecision(40.421026, -3.669247, precision);
		time = System.currentTimeMillis() - start;
		System.out.println(geohash.toBase32());
		old = geohash.toBase32();
		System.out.println("Converted in: " + time + " ms.");
		
		System.out.println("O'donnell with Menendez Pelayo");
		start = System.currentTimeMillis();
		geohash = GeoHash.withCharacterPrecision(40.421680, -3.679590, precision);
		time = System.currentTimeMillis() - start;
		System.out.println(geohash.toBase32());
		allTheSame &= old.equals(geohash.toBase32());
		old = geohash.toBase32();
		System.out.println("Converted in: " + time + " ms.");
		
		System.out.println("O'donnell with Fernan Gonzalez");
		start = System.currentTimeMillis();
		geohash = GeoHash.withCharacterPrecision(40.421190, -3.674397, precision);
		time = System.currentTimeMillis() - start;
		System.out.println(geohash.toBase32());
		allTheSame &= old.equals(geohash.toBase32());
		old = geohash.toBase32();
		System.out.println("Converted in: " + time + " ms.");
		
		System.out.println("O'donnell with Narvaez");
		start = System.currentTimeMillis();
		geohash = GeoHash.withCharacterPrecision(40.421288, -3.676028, precision);
		time = System.currentTimeMillis() - start;
		System.out.println(geohash.toBase32());
		allTheSame &= old.equals(geohash.toBase32());
		old = geohash.toBase32();
		System.out.println("Converted in: " + time + " ms.");
		
		System.out.println("Are all the points the same??? " + allTheSame);
	}

}
