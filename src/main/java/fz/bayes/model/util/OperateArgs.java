package fz.bayes.model.util;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.hadoop.conf.Configuration;

public abstract class OperateArgs {

	private   CommandLine cl;
	private  Options options;  
	
	public OperateArgs(){
		options=new Options();
	}
	
	public  String getInput(){
		return cl.getOptionValue("i");
	}

	public  String getOutput(){
		return cl.getOptionValue("o");
	}
	
	public  Configuration getConf(){
//		String jt= cl.getOptionValue("jt");
		Configuration conf= new Configuration();
//		conf.set("mapred.job.tracker", jt);
		return conf;
	}
	public  String getNameValue(String name){
		return cl.getOptionValue(name);
	}
	/**
	 * Ìí¼Ó²ÎÊý
	 * @param shortName
	 * @param longName
	 * @param hasArgs
	 * @param description
	 * @param isRequired
	 */
	public  void setOption(String shortName,String longName,boolean hasArgs,String description,boolean isRequired){
		 Option o=new Option(shortName,longName,hasArgs,description);  
        if(hasArgs){
        	o.setArgName(longName);  
        }
        o.setRequired(isRequired);  
        options.addOption(o);  
	}
	public   boolean parseArgs(String[] args)throws ParseException{  
        
        boolean flag=true;
//        setOption("jt","jobTracker",true,"the job tracker to choose",true);  
        
        setOption("i","inputPath",true,"file input path should exist",true);  
        // output   
        setOption("o","outputPath",true,"file output path should exist",true);  
        CommandLineParser parser=new PosixParser();  
        cl=null;  
        try{  
            cl=parser.parse(options, args);  
            
        }catch(Exception e){  
            System.err.println("ERROR:"+e.getMessage()+"\n");  
            HelpFormatter formatter =new HelpFormatter();  
            formatter.printHelp("\n", options,true);  
            flag=false;
        }  
        return flag;
    }

	public int run(String[] args)throws Exception {
		return 0;
	} 
	
}
