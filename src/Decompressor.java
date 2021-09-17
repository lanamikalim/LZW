import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

// this thing was made because the original code already had a decoder
// it decode my stuff since original decodes itself 
public class Decompressor {
	HashMap<Integer,String>dict = new HashMap();
	public Decompressor(int bytecount, String inputfile) throws IOException
	{
		
		for(char ch = 32;ch<=126;ch++)
		{
			dict.put((int) ch,""+ch);
		}
		File file = new File(inputfile);
		FileInputStream ree = new FileInputStream(inputfile);
		byte[] array = new byte[(int)file.length()];
		ArrayList<Integer> indexlist=new ArrayList();
		ree.read(array);
		StringBuffer builder = new StringBuffer();
		for(byte boi:array)
		{
			int indivbyte = boi & 0xff;
			
			//System.out.println(indivbyte);
			//System.out.println(Integer.toBinaryString(boi));
			String binarystring = Integer.toBinaryString(indivbyte);
			
			if(binarystring.length()<bytecount&&binarystring.charAt(0)=='1')
			{
				binarystring = String.format("%"+bytecount+"s",binarystring);
				binarystring = binarystring.replaceAll(" ","0");
				builder.append(binarystring);
			}
			else if(binarystring.length()>bytecount){
				builder.append(binarystring);
			}

		//	System.out.println(Integer.toBinaryString(boi)+"added "+builder.toString());
		}
		//this thing is the integer version of the byte
		String bytething ="";
		for(int c=0;c<builder.length();c++)
		{
			bytething+=builder.charAt(c);
			if(bytething.length()==bytecount)
			{
				indexlist.add(Integer.parseInt(bytething,2));
				
				bytething="";
				}
		}
		
		System.out.println(indexlist);
		//begin decompression algorithm
		int newestindex = 127;
		int current = indexlist.get(0);
		int next = -1;
		StringBuffer buffer = new StringBuffer();
		for(int a=0;a<indexlist.size()-1;a++)
		{
			
			String currentstr = dict.get(current);
			//System.out.println(currentstr+" at "+current);
			next = indexlist.get(a+1);
			//assumes no null shenanigans
			//working on correcting null shenanigans
			//how is there even a null shennaigans
			if(dict.containsKey(next))
			{
				
				String nextstr = dict.get(next);
				dict.put(newestindex,currentstr+nextstr.charAt(0));
				
				newestindex++;
				current=next;
				buffer.append(currentstr);
			}
			else if(currentstr !=null){
				
				//how did a null even pop up when it literally can'tmake any more IMPortantly how is there a ranndom
				dict.put(newestindex, currentstr+currentstr.charAt(0));
				//dictionarySpread();
				newestindex++;
				current=next;
				buffer.append(currentstr);
			}
			else {
				//trash code
				dict.put(newestindex,dict.get(newestindex-1)+dict.get(newestindex-1).charAt(0));
			}
			
			
		}
		buffer.append(dict.get(current));
		FileWriter writer = new FileWriter("original_of_"+inputfile+".txt");
		writer.write(buffer.toString());
		writer.close();
	
	}
	private void dictionarySpread()
	{
		for(Integer key:dict.keySet())
		{
			System.out.println("key "+key+" character "+dict.get(key));
		}
	}
}
