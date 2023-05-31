import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book_Query {
	//ͼ���ѯ����year��price�������ѯ������������Ȳ�ѯ
		static void check_Book(Connection conn) throws SQLException
		{
			Scanner reader=new Scanner(System.in);
			int choice;//����û�����ѡ��
			
			while(true){		
			    System.out.println("1.��ѯȫ�� 2.����Ų�ѯ 3.��������ѯ 4.�����߲�ѯ 5.���������ѯ 6.�������ѯ 7.����ݲ�ѯ 8.���۸��ѯ 0.�����ϼ��˵�");
			    System.out.println("��������Ҫ�ķ�����");
			    choice=reader.nextInt();
			    switch(choice)
				{
				    case 0 ://�����ϼ��˵�
						return;
					case 1://���
						//��ѯȫ��
						try {
				        String sql="select * from book";//sql���
						Statement stmt = conn.createStatement();
						ResultSet rse = stmt.executeQuery(sql);
						Book_Query.print_Book(rse);
						stmt.close();
						}catch (Exception sqle){
							System.out.println("Exception : " + sqle);
						}		
						break;  
					case 2:	
					case 3:
					case 4:
					case 5:
					case 6:
						Book_Query.equal_Book(conn,choice);break;//��ֵ��ѯ
					case 7:
					case 8:	
						Book_Query.section_Book(conn,choice);break;//�����ѯ
					default:
						System.out.println("�����Ŵ���~(T_T)~");
				}			
			}
		}	
		
	    //��ֵ��ѯ
		static void equal_Book(Connection conn,int select) throws SQLException
		{
			try {
				Scanner reader=new Scanner(System.in);
				//�����ʾ��Ϣ
				switch(select)
				{case 2:	
					System.out.println("��������ţ�");
					break;
				case 3:
					System.out.println("������������");
					break;
				case 4:
					System.out.println("���������ߣ�");
					break;
				case 5:
					System.out.println("����������磺");
					break;
				case 6:
					System.out.println("���������ࣺ");
					break;
				}	
				
				String req=reader.nextLine(); 	
				String sql="select *\r\n"+ 
						"from book\r\n" ;
				
			    //�༭sql���
				//���ݲ�ͬ��choice��Ӳ�ͬ��sql���
				switch(select)
				{
				case 2:sql=sql+"where bno=?;";break;
				case 3:sql=sql+"where title=?;";break;
				case 4:sql=sql+"where author=?;";break;
				case 5:sql=sql+"where press=?;";break;
				case 6:sql=sql+"where category=?;";break;
				}
				
				//���ò�������ѯ
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, req);
				ResultSet res = ps.executeQuery();
				
				//�����ѯ���
				Book_Query.print_Book(res);
				
				ps.close();
			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//�����ѯ
		static void section_Book(Connection conn,int select) throws SQLException
		{
			try {
				Scanner reader=new Scanner(System.in);
				String left="";
				String right="";
				String sql="select *\r\n"+ 
						"from book\r\n" ;
				//��ѯ���
				if(select==7)
				{
					System.out.println("�������ѯ�����/���ޣ��ո�ָ��");
					left=reader.next();
					right=reader.next();
					sql+="where year>=?&&year<=?;";//����sql���
				}
				//��ѯ�۸�
				else if(select==8)
				{
					System.out.println("�������ѯ�۸���/���ޣ�");
					left=reader.next();
					right=reader.next();
					sql+="where price>=?&&price<=?;";//����sql���
				}
				//���ò�������ѯ
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, left);
				ps.setString(2, right);
				ResultSet res = ps.executeQuery();
				//�����ѯ���
				Book_Query.print_Book(res);
				
				ps.close();
			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//������ѯ
		static ResultSet key_find(Connection conn,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from book\r\n" + 
					"where bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			//���ò���
			ps.setString(1, bno);
			ResultSet res= ps.executeQuery();
			//���ز�ѯ���
			return res;
		}
		//��ʽ�����book������Ϣ
		static void print_Book(ResultSet rset) throws SQLException
		{
			System.out.println("��ѯ�������");
		    System.out.println("********************************************************************************");
			System.out.println("���\t����\t����\t������\t����\t���\t�۸�\t���");
			System.out.println("********************************************************************************");
			
			while (rset.next()) { 
				System.out.println(rset.getString("bno")+"\t"+rset.getString("title")+
						"\t"+rset.getString("author")+"\t"+rset.getString("press")+
						"\t"+rset.getString("category")+"\t"+rset.getInt("year")+
						"\t"+rset.getDouble("price")+"\t"+rset.getInt("stock"));}
		}
}
