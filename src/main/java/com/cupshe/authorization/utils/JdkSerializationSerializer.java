package com.cupshe.authorization.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.springframework.core.ConfigurableObjectInputStream;
import org.springframework.core.NestedIOException;
import org.springframework.lang.Nullable;

/**
 * JDK序列化工具类
 * <p>Title: JdkSerializationSerializer</p>
 * <p>Description: </p>
 * @author zhoutaoping
 * @date 2020年10月28日
 */
public class JdkSerializationSerializer {

	static final byte[] EMPTY_ARRAY = new byte[0];
	
	public Object deserialize(@Nullable byte[] bytes) {

		if (isEmpty(bytes)) {
			return null;
		}

		try {
			return convert(bytes);
		} catch (Exception ex) {
			throw new RuntimeException("Cannot deserialize", ex);
		}
	}

	public byte[] serialize(@Nullable Object object) {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		try {
			return convert(object);
		} catch (Exception ex) {
			throw new RuntimeException("Cannot serialize", ex);
		}
	}
	
	private byte[] convert(Object source) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(1024);
		try {
			this.serialize(source, byteStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot serialize", e);
		}
		return byteStream.toByteArray();
	}
	
	private void serialize(Object object, OutputStream outputStream) throws IOException {
		if (!(object instanceof Serializable)) {
			throw new IllegalArgumentException(getClass().getSimpleName() + " requires a Serializable payload " +
					"but received an object of type [" + object.getClass().getName() + "]");
		}
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
	}
	
	private Object convert(byte[] source) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
		try {
			return this.deserialize(byteStream);
		} catch (IOException e) {
			throw new RuntimeException("Cannot deserialize", e);
		}
	}
	
	@SuppressWarnings("resource")
	private Object deserialize(InputStream inputStream) throws IOException {
		ObjectInputStream objectInputStream = new ConfigurableObjectInputStream(inputStream, null);
		try {
			return objectInputStream.readObject();
		}
		catch (ClassNotFoundException ex) {
			throw new NestedIOException("Failed to deserialize object type", ex);
		}
	}
	
	static boolean isEmpty(@Nullable byte[] data) {
		return (data == null || data.length == 0);
	}
}
