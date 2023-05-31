import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Return_Book {
	//���鹦�����˵�
	static void return_Book(Connection conn) throws SQLException
	{
		Scanner reader=new Scanner(System.in);
		System.out.println("���������,����֤���(0�����ϼ��˵�)");
		//������Ϣ
		String str=reader.nextLine();
		if(str.contentEquals("0"))return;
		//�ַ����ָ�
		String[] s = str.split("\\,");
		String bno=s[0];
		String cno=s[1];
		//��ѯborrow���Ƿ��и�����Ϣ
		ResultSet res=Borrow.key_find(conn,cno,bno);

		//borrow����û�и�����Ϣ
		if (!Book_Query.key_find(conn,bno).next()) {
		    System.out.println("����û���Ȿͼ�飡����Ҫ���Ȿ�����ͼ�����ϵͳ��1.�������ͼ��˵����������(���ᱣ�����λ�����Ϣ) 0.����Ҫ���룬����");
		    int choice=reader.nextInt();
		    //�û�ѡ�񲻼��������Ϣ
		    if(choice==0)return;
		    else Book_Add.add_onebook(conn,bno);//���������Ϣ
			
		}
		else {	
			res.next();
			if(res.getString(4)!=null)//��������и�����Ϣ���Ѿ��黹
			{
				System.out.println("�����Ѿ����أ���(������)");
				return;
			}
			try {
				//�ı�����
				String sql = "update book\r\n"
					+ " set stock=stock+1\r\n"
					+ " where bno=?";		//sql���
				PreparedStatement ps = conn.prepareStatement(sql);

				ps.setString(1, bno);
				
				ps.executeUpdate();
				
				//�޸�borrow������Ϣ���ٴν��ĸ���
				sql="update borrow\r\n"
						+ "set return_date=?\r\n"
						+ "where cno=?&&bno=?";
				ps=conn.prepareStatement(sql);
				
				//���ò���
				java.util.Date date=new java.util.Date();
				java.sql.Date date1=new java.sql.Date(date.getTime());
				
				ps.setDate(1, date1);
				ps.setString(2, cno);
				ps.setString(3, bno);

				ps.executeUpdate();
				//�ύ
				conn.commit();
				System.out.println("�ɹ����飡o(*������*)��");
			}
			catch (Exception sqle){	
				//�ع�
				conn.rollback();
				System.out.println("δ�ܳɹ�����o(�i�n�i)o");
				System.out.println("Exception : " + sqle);
			}	
		}
	}
}
