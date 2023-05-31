
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Initialize {
	//表管理主菜单
	static void table_Manage(Connection conn) throws SQLException
	{
		System.out.println("请输入功能编号:1.创建图书管理系统表 2.删除图书管理系统表 0.退出系统");
		int choice;
		Scanner reader=new Scanner(System.in);
		choice=reader.nextInt();
		
		switch(choice)
		{
		case 0:
			return;
		case 1:
			set_table(conn);
			break;
		case 2:
			drop_table(conn);
			break;
		default:
			System.out.println("服务编号错误！");
		}        
	}
	
	
	//创建图书管理系统的表
	public static void set_table(Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		try {
		//创建book表
		String sql = "create  table book\r\n" + 
				"   (bno varchar(12),  \r\n" + 
				"   title 	varchar(20),\r\n" + 
				"   author varchar(10),\r\n" + 
				"   press	varchar(20),\r\n" + 
				"   category 	varchar(10),\r\n" + 
				"   year int,\r\n" + 
				"   price	decimal(7,2),\r\n" + 
				"   stock	int,\r\n" + 
				"   primary key(bno));"; 
        stmt.executeUpdate(sql);
        
        //创建card表
        sql = "  create table card\r\n" + 
        		"  (cno varchar(6),\r\n" + 
        		"  name varchar(10),\r\n" + 
        		"  department varchar(40),\r\n" + 
        		"  type char(1),\r\n" + 
        		"  primary key(cno),\r\n" + 
        		"  check(type in('T','S')));"; 
        stmt.executeUpdate(sql);
        
        //创建borrow表
        sql = "  create table borrow\r\n" + 
        		"  (cno varchar(6),\r\n" + 
        		"  bno  varchar(12),\r\n" + 
        		"  borrow_date date,\r\n" + 
        		"  return_date date,\r\n" + 
        		"  primary key(cno,bno),\r\n" + 
        		"  foreign key (cno) references card(cno)\r\n" + 
        		"                  on delete cascade,\r\n" + 
        		"  foreign key (bno) references book(bno)\r\n" + 
        		"                  on delete cascade);"; 
        stmt.executeUpdate(sql);   
        
        //输出提示信息
        System.out.println("********************************************");
		System.out.println("\t图书管理系统初始表格创建完毕！");
		System.out.println("********************************************");
		}
		//处理异常
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
		
	}
	
	//删除图书管理系统的表
	public static void drop_table(Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		try
		{
		//删除所有表
		String sql = "drop table borrow;\r\n"; 
        stmt.executeUpdate(sql);
        sql = "drop table card;\r\n"; 
        stmt.executeUpdate(sql);
        sql = "drop table book;\r\n"; 
        stmt.executeUpdate(sql);
        
        //输出提示信息
        System.out.println("********************************************");
		System.out.println("\t图书管理系统表格删除完毕！");
		System.out.println("********************************************");
		}
		//处理异常
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
        
	}
	
	//数据管理主菜单
	static void data_Manage(Connection conn) throws SQLException, FileNotFoundException
	{
		System.out.println("请输入功能编号:1.载入初始数据 2.删除所有数据 0.退出系统");
		int choice;
		Scanner reader=new Scanner(System.in);
		choice=reader.nextInt();
		
		switch(choice)
		{
		case 0:
			return;
		case 1:
			load_Data(conn);
			break;
		case 2:
			delete_Data(conn);
			break;
		default:
			System.out.println("服务编号错误！");
		}        
	}
	//从文件中批量导入数据
	static void load_Data(Connection conn) throws FileNotFoundException, SQLException
	{
		Book_Add.file_addbook(conn,"book.txt");
		Proof.file_addcard(conn,"card.txt");
		Borrow.file_addborrow(conn,"borrow.txt");
	}
	//清空数据
	static void delete_Data(Connection conn) throws SQLException
	{
		while(true)
		{
		System.out.println("1.清空所有表中的数据 2.清空borrow表 3.清空card表 4.清空book表0.返回上级菜单");
		System.out.println("（请注意：borrow的外键引用设置级联删除，因此book/card表的清空也会导致borrow表的清空！）");
		System.out.println("请输入需要的服务编号:");
		Scanner reader=new Scanner(System.in);
		int choice;
		choice=reader.nextInt();
		
		//根据不同的选择对不同的表做清空处理
		switch(choice)
		{
		case 0:
			return;
		case 1:
			delete_tabledata(conn,"borrow");
			delete_tabledata(conn,"card");
			delete_tabledata(conn,"book");
			break;
		case 2:
			delete_tabledata(conn,"borrow");
			break;
		case 3:
			delete_tabledata(conn,"card");
			break;
		case 4:
			delete_tabledata(conn,"book");
			break;
		default:
			System.out.println("服务编号错误~(T_T)~");			
		}
		}
		
	}
	//清除某个表中的全部数据
	static void delete_tabledata(Connection conn,String table) throws SQLException
	{
		Statement stmt=conn.createStatement();
		try
		{//删除table表
		String sql = "set sql_safe_updates=0;";
		stmt.execute(sql);
		sql="delete from book;"; 
		PreparedStatement ps;
		ps=conn.prepareStatement(sql);
		ps.executeUpdate();    
		System.out.println("图书管理系统"+table+"表中数据删除完毕！");		
		}catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
        
	}
	
}
