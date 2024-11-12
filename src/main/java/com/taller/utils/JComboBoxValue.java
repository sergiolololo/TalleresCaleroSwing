package com.taller.utils;

public class JComboBoxValue {

    private final int id;
	private final String value;

    public JComboBoxValue(int key, String value) {
        this.id = key;
        this.value = value;
    }

	@Override
    public String toString() {
        return value;
    }
	
	public int getId() {
		return id;
	}
	public String getValue() {
		return value;
	}
}