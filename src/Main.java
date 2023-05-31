//头文件

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

//主程序
public class Main {
	
	static Connection conn;
	static Statement stmt;
	//main函数，程序起始点
	public static void main(String args[]){		
		Scanner reader=new Scanner(System.in);

		//读入数据库连接信息
		
		System.out.println("请输入数据库名称:");
		String dbname=reader.nextLine();
		System.out.println("请输入用户名:");
		String username=reader.nextLine();
		System.out.println("请输入密码:");
		String password=reader.nextLine();
		start(dbname,username,password);
		
		
		//start("jdbctest","root","mysql2020@Ylpy");
	}
	public static void start(String dbname,String userid, String passwd) { 
		try{
			//连接数据库
			Class.forName ("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(//&useSSL=true
					"jdbc:mysql://localhost:3306/"
					+ dbname
					+ "?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
					userid, passwd);			
			
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			

			System.out.println("********************************************************");
			System.out.println("\t\t\t图书管理系统");
			System.out.println("********************************************************");
			
			Scanner reader=new Scanner(System.in);
			
			while(true){
				try {
					System.out.println("1.图书查询 2.借书记录查询 3.借书 4.还书 5.图书入库 6.借阅证管理 7.数据管理 8.表管理 0.退出系统");
					System.out.println("请输入需要的服务编号:");
					
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
							System.out.println("服务编号错误~(T_T)~");
					}
				}
				//处理异常
				catch (Exception sqle){
					System.out.println("Exception : " + sqle);
			    }
			}	
		}
		//处理异常
		catch (Exception sqle){
			System.out.println("未能成功连接数据库！");
			System.out.println("Exception : " + sqle);
		}
	}

}
