import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Borrow_Book {
	//���鹦�����˵�
	static void borrow_Book(Connection conn) throws SQLException
	{
		Statement stmt=conn.createStatement();
		Scanner reader=new Scanner(System.in);
		
		System.out.println("���������,����֤���(0�����ϼ��˵�)");
		//��ȡ��ϢΪ�ַ���
		String str=reader.nextLine();
        //�������0���򷵻��ϼ��˵�
		if(str.contentEquals("0"))return;
		//�ָ��ַ���
		String[] s = str.split("\\,");
		String bno=s[0];
		String cno=s[1];
        
		//�ӱ��в�ѯ��Ϣ�������жϵ�ǰ���������
		ResultSet res= Book_Query.key_find(conn,bno);
		ResultSet res1=Borrow.key_find(conn,cno,bno);
		ResultSet res2=Borrow.key_findbno(conn,bno);
		ResultSet res3=Proof.key_find(conn,cno);
		
		//���book����û�иñ��
		if (!res.next()) {
		    System.out.println("ͼ�����û�б��Ϊ������Ϣ��ͼ�飡");	
		} 
		//book�����и�����Ϣ���������Ϊ0
		else if(res.getInt("stock")==0){
			System.out.println("����Ŀ��п��Ϊ0��");//�����ʾ��Ϣ
			//���borrow�����и�����Ϣ
			if(res2.next())
			{
				//����������еĽ�����Ϣ
				Borrow.print_Borrow(res2);	
			}
		}
		//�������û�иı�ŵĽ��鿨
		else if(!res3.next())
		{
			System.out.println("ͼ�����û�б��Ϊ������Ϣ�Ľ���֤��");	
		}
		//���borrow���Ӳ��޸���������Ϣ
		else if(!res1.next()){
			try {
				//����ԭ����
				
				//�ı�book�и�������
				String sql = "update book"
					+ " set stock=stock-1"
					+ " where bno=?";		
				PreparedStatement ps;
				ps = conn.prepareStatement(sql);

				ps.setString(1, bno);
				
				ps.executeUpdate();
				
				//������Ϣ��borrow
				sql="insert into borrow values"
						+ "(?,?,?,?);";
				
				ps=conn.prepareStatement(sql);
				//���ò���
				ps.setString(1, cno);
				ps.setString(2, bno);
				java.util.Date date=new java.util.Date();
				java.sql.Date date1=new java.sql.Date(date.getTime());			
				ps.setDate(3, date1);
				ps.setNull(4, java.sql.Types.DATE);

				ps.executeUpdate();
				//�ύ
				conn.commit();
				System.out.println("�ɹ����飡(*^��^*)");
			}
			catch (Exception sqle){	
				//�ع�
				conn.rollback();
				System.out.println("����ʧ�ܣ�(��o��) ");
				System.out.println("Exception : " + sqle);
			}	
		}	
		else//���borrow�����и�����Ϣ
		{
			//���������Ϣ�в�δ�黹
			if(res1.getString(4)==null)
			{
				System.out.println("����"+cno+"���"+bno+"�ÿ��ѽ��Ĺ����飡���һ�δ�黹���t���F���䣩�s ");	
			}
			else//����Ѿ����Ĳ��黹
			{
				System.out.println("����"+cno+"���"+bno+"�ÿ��ѽ��Ĺ����飡�����Ƿ��ٴν���? 1.�� 0.�񣬷����ϼ��˵�");
				int choice;
				choice=reader.nextInt();
				if(choice==1)
				{
					//�ٴν��ĸ���
					try {
					//�޸Ŀ����
					String sql = "update book\r\n"
							+ " set stock=stock-1\r\n"
							+ " where bno=?";		
					PreparedStatement ps;
					ps = conn.prepareStatement(sql);
					//���ò���
					ps.setString(1, bno);	
					
					ps.executeUpdate();
					
					//����borrow�е���Ϣ
					sql="update borrow\r\n" + 
							"set borrow_date=?,return_date=?\r\n" + 
							"where cno=?&& bno=?";
					
					ps=conn.prepareStatement(sql);
					
					//���ò���			
					java.util.Date date=new java.util.Date();
					java.sql.Date date1=new java.sql.Date(date.getTime());
					
					ps.setDate(1, date1);
					ps.setNull(2, java.sql.Types.DATE);				
					ps.setString(3, cno);
					ps.setString(4, bno);

					ps.executeUpdate();
					//�ύ
					conn.commit();
					System.out.println("�ɹ����飡(*^��^*)");
					}catch (Exception sqle){	
						//�ع�
						conn.rollback();
						System.out.println("����ʧ�ܣ�(��o��) ");
						System.out.println("Exception : " + sqle);
					}
				}	
				
			}
		}
		stmt.close();
	}
	
	
}
