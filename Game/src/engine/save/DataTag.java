package engine.save;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DataTag {

	private JSONObject data;

	@Override
	public String toString() {
		return data.toJSONString();
	}

	public DataTag(JSONObject data){
		this.data = data;
	}

	public DataTag() {
		data = new JSONObject();
	}

	public JSONObject getData(){ return data; }

	public boolean hasTag(String tag){
		return data.containsKey(tag);
	}

	@SuppressWarnings("unchecked")
	public void writeInt(String tag, int i){
		data.put(tag, i);
	}


	@SuppressWarnings("unchecked")
	public void writeList(String tag, DataList list){
		if(list != null)
			data.put(tag, list.data());

	}

	public DataList readList(String tag){

		if(!data.containsKey(tag)){
			System.out.println("The tag "+ tag + " did not exist.");
			return null;
		}

		DataList list = new DataList((JSONArray)data.get(tag));

		return list;
	}


	public int readInt(String tag){
		long l = 0;

		if(!data.containsKey(tag)){
			System.out.println("The tag "+ tag + " did not exist.");
			return 0;
		}

		l = (Long)data.get(tag);

		return (int)l;
	}

	@SuppressWarnings("unchecked")
	public void writeDouble(String tag, double d){
		data.put(tag, d);
	}

	public double readDouble(String tag){
		double d = 0;

		if(!data.containsKey(tag)){
			System.out.println("The tag "+ tag + " did not exist.");
			return d;
		}

		d = (Double)data.get(tag);

		return d;
	}

	@SuppressWarnings("unchecked")
	public void writeFloat(String tag, float f){
		data.put(tag, f);
	}

	public float readFloat(String tag){
		float f = 0;

		if(!data.containsKey(tag)){
			System.out.println("The tag "+ tag + " did not exist.");
			return f;
		}
		double d = (double)data.get(tag);
		f = (float)d;

		return f;
	}

	@SuppressWarnings("unchecked")
	public void writeString(String tag, String s){
		data.put(tag, s);
	}

	public String readString(String tag){
		String s = "";

		if(!data.containsKey(tag)){
			System.out.println("The tag "+ tag + " did not exist.");
			return s;
		}

		s = (String)data.get(tag);

		return s;
	}

	@SuppressWarnings("unchecked")
	public void writeBoolean(String tag, boolean flag){
		Byte b;
		if(flag)
			b = 1;
		else
			b = 0;

		data.put(tag, b);
	}

	public boolean readBoolean(String tag){

		Object o = data.get(tag);

		if(o instanceof Byte){
			Byte b = (Byte)o;

			return b == 0 ? false : true;
		}

		return o.toString().equals("0") ? false : true;
	}
}
