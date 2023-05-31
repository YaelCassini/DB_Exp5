
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Initialize {
	//��������˵�
	static void table_Manage(Connection conn) throws SQLException
	{
		System.out.println("�����빦�ܱ��:1.����ͼ�����ϵͳ�� 2.ɾ��ͼ�����ϵͳ�� 0.�˳�ϵͳ");
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
			System.out.println("�����Ŵ���");
		}        
	}
	
	
	//����ͼ�����ϵͳ�ı�
	public static void set_table(Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		try {
		//����book��
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
        
        //����card��
        sql = "  create table card\r\n" + 
        		"  (cno varchar(6),\r\n" + 
        		"  name varchar(10),\r\n" + 
        		"  department varchar(40),\r\n" + 
        		"  type char(1),\r\n" + 
        		"  primary key(cno),\r\n" + 
        		"  check(type in('T','S')));"; 
        stmt.executeUpdate(sql);
        
        //����borrow��
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
        
        //�����ʾ��Ϣ
        System.out.println("********************************************");
		System.out.println("\tͼ�����ϵͳ��ʼ��񴴽���ϣ�");
		System.out.println("********************************************");
		}
		//�����쳣
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
		
	}
	
	//ɾ��ͼ�����ϵͳ�ı�
	public static void drop_table(Connection conn) throws SQLException
	{
		Statement stmt = conn.createStatement();
		try
		{
		//ɾ�����б�
		String sql = "drop table borrow;\r\n"; 
        stmt.executeUpdate(sql);
        sql = "drop table card;\r\n"; 
        stmt.executeUpdate(sql);
        sql = "drop table book;\r\n"; 
        stmt.executeUpdate(sql);
        
        //�����ʾ��Ϣ
        System.out.println("********************************************");
		System.out.println("\tͼ�����ϵͳ���ɾ����ϣ�");
		System.out.println("********************************************");
		}
		//�����쳣
		catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
        
	}
	
	//���ݹ������˵�
	static void data_Manage(Connection conn) throws SQLException, FileNotFoundException
	{
		System.out.println("�����빦�ܱ��:1.�����ʼ���� 2.ɾ���������� 0.�˳�ϵͳ");
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
			System.out.println("�����Ŵ���");
		}        
	}
	//���ļ���������������
	static void load_Data(Connection conn) throws FileNotFoundException, SQLException
	{
		Book_Add.file_addbook(conn,"book.txt");
		Proof.file_addcard(conn,"card.txt");
		Borrow.file_addborrow(conn,"borrow.txt");
	}
	//�������
	static void delete_Data(Connection conn) throws SQLException
	{
		while(true)
		{
		System.out.println("1.������б��е����� 2.���borrow�� 3.���card�� 4.���book��0.�����ϼ��˵�");
		System.out.println("����ע�⣺borrow������������ü���ɾ�������book/card������Ҳ�ᵼ��borrow�����գ���");
		System.out.println("��������Ҫ�ķ�����:");
		Scanner reader=new Scanner(System.in);
		int choice;
		choice=reader.nextInt();
		
		//���ݲ�ͬ��ѡ��Բ�ͬ�ı�����մ���
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
			System.out.println("�����Ŵ���~(T_T)~");			
		}
		}
		
	}
	//���ĳ�����е�ȫ������
	static void delete_tabledata(Connection conn,String table) throws SQLException
	{
		Statement stmt=conn.createStatement();
		try
		{//ɾ��table��
		String sql = "set sql_safe_updates=0;";
		stmt.execute(sql);
		sql="delete from book;"; 
		PreparedStatement ps;
		ps=conn.prepareStatement(sql);
		ps.executeUpdate();    
		System.out.println("ͼ�����ϵͳ"+table+"��������ɾ����ϣ�");		
		}catch (Exception sqle){
			System.out.println("Exception : " + sqle);
		}
		stmt.close();
        
	}
	
}
