package com.mobiosis.persistencesample.utils;

import java.lang.reflect.Type;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GsonUtils {
	private static class UriDeserializer implements 
	JsonDeserializer<Uri>, 
	JsonSerializer<Uri>{
		@Override
		public Uri deserialize(JsonElement src, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			return Uri.parse(src.getAsString());
		}

		@Override
		public JsonElement serialize(Uri src, Type type,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}
		
	}

	private static Gson mGson;
	
	synchronized public static Gson getGson() {
		if (mGson == null) {
			mGson = new GsonBuilder()
		    .registerTypeAdapter(Uri.class, new UriDeserializer())
		    .create();
		}
		return mGson;
	}

}
