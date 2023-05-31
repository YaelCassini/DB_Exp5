//ͷ�ļ�

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

//������
public class Main {
	
	static Connection conn;
	static Statement stmt;
	//main������������ʼ��
	public static void main(String args[]){		
		Scanner reader=new Scanner(System.in);

		//�������ݿ�������Ϣ
		
		System.out.println("���������ݿ�����:");
		String dbname=reader.nextLine();
		System.out.println("�������û���:");
		String username=reader.nextLine();
		System.out.println("����������:");
		String password=reader.nextLine();
		start(dbname,username,password);
		
		
		//start("jdbctest","root","mysql2020@Ylpy");
	}
	public static void start(String dbname,String userid, String passwd) { 
		try{
			//�������ݿ�
			Class.forName ("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(//&useSSL=true
					"jdbc:mysql://localhost:3306/"
					+ dbname
					+ "?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
					userid, passwd);			
			
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			

			System.out.println("********************************************************");
			System.out.println("\t\t\tͼ�����ϵͳ");
			System.out.println("********************************************************");
			
			Scanner reader=new Scanner(System.in);
			
			while(true){
				try {
					System.out.println("1.ͼ���ѯ 2.�����¼��ѯ 3.���� 4.���� 5.ͼ����� 6.����֤���� 7.���ݹ��� 8.����� 0.�˳�ϵͳ");
					System.out.println("��������Ҫ�ķ�����:");
					
					int choice=reader.nextInt();
					switch(choice)
					{
					    case 0 :
							conn.close();
							return;
						case 1:
							Book_Query.check_Book(conn);
							break;
						case 2:
							Borrow.check_Record(conn);
							break;
					    case 3:
							Borrow_Book.borrow_Book(conn);
							break;
						case 4:
							Return_Book.return_Book(conn);
							break;
						case 5:
							Book_Add.add_Book(conn);
							break;
						case 6:
							Proof.proof_Manag(conn);
						    break;
						case 7:
							Initialize.data_Manage(conn);
							break;
						case 8:
							Initialize.table_Manage(conn);
							break;
						default:
							System.out.println("�����Ŵ���~(T_T)~");
					}
				}
				//�����쳣
				catch (Exception sqle){
					System.out.println("Exception : " + sqle);
			    }
			}	
		}
		//�����쳣
		catch (Exception sqle){
			System.out.println("δ�ܳɹ��������ݿ⣡");
			System.out.println("Exception : " + sqle);
		}
	}

}
