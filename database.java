/*Prepare a Java Program to insert 200 records as followings conditions
1) Create 200 vouchers 
2) vouchers amount is multiples of 50
3) 4 voucher for each amount
4) if voucher amount is multiple of 100 then benefit is 100% otherwise it will be 90%
5) voucher created date should be today and expiry after 1 year 
6) voucher number should be random number 14-16 digit 
7) add message for each voucher
8) for all voucher status should be save as 1
Table Structure 
voucher_id                                   int                              Auto Increment
Voucher_code                             varchar
amount                                        double
benefit_amount                           double
created_date                               datetime
expiry_date                                 datetime
message                                     varchar
status                                          varchar(1)         
 */
	
	
	import java.sql.DriverManager;
	import java.sql.*;
	import java.util.Random;
	import java.util.concurrent.ThreadLocalRandom;
	import java.util.Date;
	import java.text.SimpleDateFormat;
	import java.sql.DataTruncation;
	
	//@SuppressWarnings("deprecation")
	
	class voucher
	{
	private long voucher_code;
	private static String status;
	private double amount;
	private String message;
	private static final Random rand=new Random();
	private static Connection cn=null;
	static{
			try{
	
				status="1";
				//Class.forName("com.mysql.jdbc.Driver");
				cn=DriverManager.getConnection("jdbc:mysql://localhost/voucher","root","");
				if(cn!=null)
				{
					System.out.println("Connetion Established");
		
				}
			}
			catch(Exception e)
			{
				System.out.println("Occurs : "+e);
			}
		}
	public voucher(double amount) 
	{
		this.amount=amount;
		insert();
	}
	private String mess()
		{
			String s=null;
			int c=rand.nextInt(11);
			switch(c)
			{
				case 1: s="message 1-message-1";
						break;
				case 2: s="message 2-message-2";
						break;
				case 3: s="message 3-message-3";
						break;		
				case 4: s="message 4-message-4";
						break;
				case 5: s="message 5-message-5";
						break;
				case 6: s="message 6-message-6";
						break;
				case 7: s="message 7-message-7";
						break;
				case 8: s="message 8-message-8";
						break;
				case 9: s="message 9-message-9";
						break;
				case 10: s="message 10-message-10";
						break;
			}
			return s;
		}
		private long vcodegen() {
			long small=10000000000000l;
			long big=9999999999999999l;
			long randd=ThreadLocalRandom.current().nextLong(small,big);
			return randd;
		}
	private double benifit_amount(double amount)
	{
		if(amount%100==0)
		{
			return(amount*1);
		}
		return (amount*0.90);
	}
public void insert()
	{	
	try {
		int d=0,m=0,y=0;
		int ey=0;
		String [] dt=new String[2];

		d=rand.nextInt(32);
		m=rand.nextInt(13);
		y=ThreadLocalRandom.current().nextInt(2000,2038);

		//for leap year
		if(d==0)
		{
			d=1;	
		}
		if(m==0)
			m=1;

		if((d==29) && (m==2) && (y%4==0))
		{
			dt[0]=y+"-"+m+"-"+d;
			//after one year
			ey=++y;d=28;
			dt[1]=ey+"-"+m+"-"+d;
		}
		else{
			
			dt[0]=y+"-"+m+"-"+d;
			//after one year
			ey=++y;
			dt[1]=ey+"-"+m+"-"+d;
			}
		//for converting datetime
SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Date cdate=df.parse(dt[0]);
		Date eDate=df.parse(dt[1]);
		
		long vcd=vcodegen();
		double am=this.amount;
		double bam=benifit_amount(am);
	
	//matching the date with sql format...
	java.sql.Date cudate=new java.sql.Date(cdate.getTime());
	java.sql.Date exDate=new java.sql.Date(eDate.getTime());		

	
String query="INSERT INTO voucher1(voucher_code,amount,benifit_amount,created_date,expiry_date,message,status) VALUES(?,?,?,?,?,?,?);";

PreparedStatement stm=cn.prepareStatement(query);
		stm.setLong(1,vcd);
		stm.setDouble(2,am);
		stm.setDouble(3,bam);
		stm.setDate(4,cudate);
		stm.setDate(5,exDate);
		stm.setString(6,mess());
		stm.setString(7,status);
		stm.executeUpdate();
	}
	catch(Exception e)
		{
			System.out.println(e);
		}
	}
public void select(){
	try{
	Statement st=cn.createStatement();
	String query="select * from voucher1;";
	ResultSet rst=st.executeQuery(query);
	while(rst.next())
	{
		System.out.println(rst.getInt(1)+"|"+rst.getString(2)+"|"+rst.getDouble(3)+"|"+rst.getDouble(4)+"|"+rst.getDate(5)+"|"+rst.getDate(6)+"|"+rst.getString(7)+"|"+rst.getString(8));
	}
	}
	catch(Exception e){
		System.out.println(e);
	}
}	
public void finilize()
{
	try{cn.close();}
	catch(Exception e){System.out.println(e);}
	
}
}

	//main
	class database
	{
		static double amount;
		static{
			amount=50;
		}
		public static void main(String[] args) {

			//for inserting the data
			voucher v[]=new voucher[200];			
			for(int i=0,j=1;i<200;i++,j++)
			{
				v[i]=new voucher(amount);
					if(j%4==0)
						amount+=50;
			}
			//for fetching the data
		voucher q=new voucher();
			q.select();
		}

	}