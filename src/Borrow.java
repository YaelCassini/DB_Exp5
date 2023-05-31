
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Borrow {
	    //��ѯ�����¼
		static void check_Record(Connection conn) throws SQLException, FileNotFoundException
		{
			Scanner reader=new Scanner(System.in);
			int choice;//����û�ѡ��		
			
			while(true){
			    System.out.println("1.��ѯȫ�� 2.����Ų�ѯ 3.������֤��Ų�ѯ 4.����������ļ�¼ 0.�����ϼ��˵�");
			    System.out.println("��������Ҫ�ķ�����");
			    choice=reader.nextInt();
			    switch(choice)//ѡ��ͬ�Ĳ�ѯ��ʽ
				{
				    case 0 ://���
						return;
					case 1://���
						try {
							//��ѯȫ��
						    String sql="select * from borrow";
						    Statement stmt=conn.createStatement();
						    ResultSet rse = stmt.executeQuery(sql);				
						    Borrow.print_Borrow(rse);		
						    stmt.close();
						}catch (Exception sqle){
							System.out.println("Exception : " + sqle);
						}
						break;  
					case 2:	
						//�����Ų�ѯ
						Borrow.cno_Record(conn);
						break;
					case 3:
						//����Ų�ѯ
						Borrow.bno_Record(conn);
						break;
					case 4://������ӽ�����Ϣ
						System.out.println("�������ļ�����");
				    	reader.nextLine();
				    	String borrow=reader.nextLine();
						file_addborrow(conn,borrow);
						break;
					default:
						System.out.println("�����Ŵ���~(T_T)~");
				}					
			}
		}
	
	    //ͨ�����鿨�Ų�ѯ�����¼
		static void cno_Record(Connection conn) throws SQLException
		{
			try {
			    Scanner reader=new Scanner(System.in);	
			    
				System.out.println("��������Ҫ��ѯ�Ľ���֤(0�����ϼ��˵�)��");
				String cno=reader.nextLine();
				if(cno.contentEquals("0"))return;
				else
				{
					ResultSet res=Borrow.key_findcno(conn, cno);  //key��ѯ
					Borrow.print_Borrow(res);	//���
				}

			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		
		static void bno_Record(Connection conn) throws SQLException//����֤��ѯ�����¼
		{
			
			try {
			    Scanner reader=new Scanner(System.in);	

			    System.out.println("��������Ҫ��ѯ�Ľ���֤(0�����ϼ��˵�)��");
				String bno=reader.nextLine();
				if(bno.contentEquals("0"))return;
				else
				{
					ResultSet res=Borrow.key_findbno(conn, bno);
					Borrow.print_Borrow(res);	
				}

			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//���ļ����������
		static void file_addborrow(Connection conn,String filename) throws FileNotFoundException, SQLException
		{
			File InputFile = new File(filename); //����file���ȡ�ļ�
			if(InputFile.exists())
			{
				Scanner input = new Scanner(InputFile); //������file�Ķ�������һ��scanner����
	            
				while(input.hasNext()){//���ж�ȡ�����ļ���Ϣ
					String cno= input.next();
					String bno = input.next();
					String borrow_date = input.next();
					String return_date = input.next();
					
					ResultSet record=Borrow.key_find(conn,cno,bno);
					if(record.next())//�������и��������¼
					{
						System.out.println("����"+cno+"���"+bno+"�ÿ��ѽ��Ĺ����飡�t���F���䣩�s ");	
						
					}
					else{
						try {
							//������Ϣ��book
							String sql="insert into borrow values"
									+ "(?,?,?,?)";
							PreparedStatement ps;
								
							ps=conn.prepareStatement(sql);
							
							//���ò���
							ps.setString(1, cno);
							ps.setString(2, bno);
							ps.setString(3, borrow_date);	
							if(return_date.equals("null")) ps.setString(4, null);
							else ps.setString(4, return_date);			
							
							ps.executeUpdate();
							//�ύ
							conn.commit();
							}catch (Exception sqle){
								//�ع�
								conn.rollback();
								System.out.println("����"+cno+"���"+bno+"δ�ܳɹ�������У����n��");	
								System.out.println("Exception : " + sqle);
							}		 
					}		
				}
				System.out.println("�ɹ������ļ��е�ȫ��������Ϣ��\\(�R���Q*)o");
		    	input.close();
			}
			else
			{
				System.out.println("�Ҳ������ļ������n��");	
			}
		}
		//���ݽ���֤���Ų�ѯ������Ϣ
		static ResultSet key_findcno(Connection conn,String cno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where cno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
            //���ò���
			ps.setString(1, cno);
			ResultSet res= ps.executeQuery();
			//���ز�ѯ���
			return res;
		}
		//������Ų�ѯ������Ϣ
		static ResultSet key_findbno(Connection conn,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
            //���ò���
			ps.setString(1, bno);
			ResultSet res= ps.executeQuery();
			//���ز�ѯ���
			return res;
		}
		//������źͽ��Ŀ��Ų�ѯ������Ϣ
		static ResultSet key_find(Connection conn,String cno,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where cno=?&&bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			//���ò���
			ps.setString(1, cno);
			ps.setString(2, bno);
			ResultSet res= ps.executeQuery();
			//���ز�ѯ���
			return res;
		}
		//��ʽ�����borrow������Ϣ
		static void print_Borrow(ResultSet rset) throws SQLException
		{
			System.out.println("��ѯ�������");
		    System.out.println("********************************************************************************");
			System.out.println("���鿨��\t���\t��������\t\t��������");
			System.out.println("********************************************************************************");
			
			while (rset.next()) { 
				System.out.println(rset.getString("cno")+"\t"+rset.getString("bno")+
						"\t"+rset.getString("borrow_date")+"\t"+rset.getString("return_date"));
			}
		}
}
