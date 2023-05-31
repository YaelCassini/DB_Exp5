import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Book_Add {
	//ͼ��������˵�
	static void add_Book(Connection conn) throws SQLException, FileNotFoundException//����ʵ��   //ֻ�м۸��ǿղ����ᵼ���׳��쳣
	{
		while(true){
		Scanner reader=new Scanner(System.in);
		int choice;
		System.out.println("1.������� 2.���ļ���� 0.�����ϼ��˵�");
		System.out.println("��������Ҫ�ķ�����");
		choice=reader.nextInt();
		
		switch(choice)
		{
		    case 0 ://���
				return;
			case 1:
				try
				{
					reader.nextLine();//��ȡ����Ļس�
					while(true)
					{
					System.out.println("��������Ҫ��ӵ����(0�˳�,������һ���˵�)");
					String bno=reader.nextLine();
					//�������0�򷵻���һ���˵�
					if(bno.contentEquals("0"))break;
					
					//book�в��Ҳ������Ϊbno����Ϣ
					if(!Book_Query.key_find(conn,bno).next())
					{
						System.out.println("���л�û�и�����Ϣ��");
						//����һ���µ�ͼ����Ϣ
						add_onebook(conn,bno);
					}
					else
					{
						//���и�����Ϣ��ֻ�����ӿ��
						add_stock(conn,bno,1);
					}					
				}
				}catch (Exception sqle){
					System.out.println("Exception : " + sqle);
				}
				break;
			case 2:
				//�����ļ���
				String filename;
				System.out.println("�������ļ�����");
				reader.nextLine();
				filename=reader.nextLine();
				//���ļ��ж�ȡͼ����Ϣ
				file_addbook(conn,filename);
				break;
			default:
				//���������Ϣ
				System.out.println("�����Ŵ���~(T_T)~");				
    	}	
		}
	}
	
	static void add_onebook(Connection conn,String bno) throws SQLException
	{
		Scanner reader=new Scanner(System.in);
		try {
			System.out.println("����������,����,������,����,���,�۸�(0�����ϻ��˵�)");
            //��ȡͼ����Ϣ
			String str=reader.nextLine();
			//�ָ��ַ������õ��������Ե���Ϣ
			String[] s = str.split("\\,");
			String title = s[0];
			String author = s[1];
			String press = s[2];
			String category = s[3];
			String year=s[4];
			String price=s[5];
			
			//������Ϣ��book
			String sql="insert into book values"
					+ "(?,?,?,?,?,?,?,?)";
			PreparedStatement ps;				
			ps=conn.prepareStatement(sql);
			
			//���ò���
			ps.setString(1, bno);
			
			if(title.equals("")) ps.setString(2, null);
			else ps.setString(2, title);
			if(author.equals("")) ps.setString(3, null);
			else ps.setString(3, author);
			if(press.equals("")) ps.setString(4, null);
			else ps.setString(4, press);
			if(category.equals("")) ps.setString(5, null);
			else ps.setString(5, category);
			if(year.equals("")) ps.setString(6, null);
			else ps.setInt(6, Integer.parseInt(year));
			if(price.equals("")) ps.setString(7, null);
			else ps.setString(7, price);
			ps.setInt(8, 1);	
			
			//�޸����ݿ���Ϣ
			ps.executeUpdate();
			//�ύ
			conn.commit();
			System.out.println("�ѳɹ��������Ӹ�����Ϣ��~/(��o��)/~");		
		}catch (Exception sqle){
			//�ع�
			conn.rollback();
			System.out.println("δ�ܳɹ����������Ϣ��:( ");	
			System.out.println("Exception : " + sqle);
		}	
	}
	//����µ�ͼ����Ϣ
	static void add_stock(Connection conn,String bno,int number) throws SQLException
	{
		try {
			String sql="update book\r\n"
					+ "set stock=stock+?\r\n"
					+ "where bno=?;\r\n";
			
			PreparedStatement ptmt1 = conn.prepareStatement(sql);
			//���ò���
			ptmt1.setInt(1, number);
			ptmt1.setString(2, bno);
			
			ptmt1.executeUpdate();
			//�ύ
			conn.commit();		
			System.out.println("���"+bno+"�������и�����¼���ѳɹ���ӿ�棡");
			
		}catch (Exception sqle){	
			//�ع�
			conn.rollback();
			System.out.println("���"+bno+"�������и�����¼����δ�ɹ���ӿ�棡");
			System.out.println("Exception : " + sqle);
		}
	}
	//���ļ������ͼ����Ϣ
	static void file_addbook(Connection conn,String filename) throws FileNotFoundException, SQLException
	{
		File InputFile = new File(filename); //����File���ȡ�ļ�
		if(InputFile.exists())
		{
			Scanner input = new Scanner(InputFile); //��File�Ķ�������һ��scanner����
            
			while(input.hasNext()){
				//���ļ��ж�ȡ��������
				String bno= input.next();
				String title = input.next();
				String author = input.next();
				String press = input.next();
				String category = input.next();
				String year= input.next();
				String price= input.next();
				String stock= input.next();
				
				if(Book_Query.key_find(conn,bno).next())//�������и��飬ֻ�����ӿ��
				{
					add_stock(conn,bno,Integer.parseInt(stock));
				}
				else{
					try {
						//������Ϣ��book
						String sql="insert into book values"
								+ "(?,?,?,?,?,?,?,?)";
						PreparedStatement ps;
							
						ps=conn.prepareStatement(sql);
						
						//���ò���
						ps.setString(1, bno);
						
						if(title.equals("null")) ps.setString(2, null);
						else ps.setString(2, title);
						if(author.equals("null")) ps.setString(3, null);
						else ps.setString(3, author);
						if(press.equals("null")) ps.setString(4, null);
						else ps.setString(4, press);
						if(category.equals("null")) ps.setString(5, null);
						else ps.setString(5, category);
						if(year.equals("null")) ps.setString(6, null);
						else ps.setInt(6, Integer.parseInt(year));
						if(price.equals("null")) ps.setString(7, null);
						else ps.setString(7, price);
						ps.setInt(8, Integer.parseInt(stock));				
						
						ps.executeUpdate();
						//�ύ
						conn.commit();
					}catch (Exception sqle){
						//�ع�
						conn.rollback();
						System.out.println("���"+bno+"δ�ܳɹ�������У����n��");	
						System.out.println("Exception : " + sqle);
					}		 
				}		
			}
			System.out.println("�ɹ������ļ��е�ȫ��ͼ����Ϣ��\\(�R���Q*)o");
	    	input.close();
		}
		else
		{
			System.out.println("�Ҳ������ļ������n��");	
		}
	}
}
