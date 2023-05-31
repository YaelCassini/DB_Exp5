import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book_Query {
	//图书查询除了year和price是区间查询，其他都是相等查询
		static void check_Book(Connection conn) throws SQLException
		{
			Scanner reader=new Scanner(System.in);
			int choice;//存放用户功能选择
			
			while(true){		
			    System.out.println("1.查询全部 2.按书号查询 3.按书名查询 4.按作者查询 5.按出版社查询 6.按种类查询 7.按年份查询 8.按价格查询 0.返回上级菜单");
			    System.out.println("请输入需要的服务编号");
			    choice=reader.nextInt();
			    switch(choice)
				{
				    case 0 ://返回上级菜单
						return;
					case 1://完成
						//查询全部
						try {
				        String sql="select * from book";//sql语句
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
						Book_Query.equal_Book(conn,choice);break;//等值查询
					case 7:
					case 8:	
						Book_Query.section_Book(conn,choice);break;//区间查询
					default:
						System.out.println("服务编号错误~(T_T)~");
				}			
			}
		}	
		
	    //等值查询
		static void equal_Book(Connection conn,int select) throws SQLException
		{
			try {
				Scanner reader=new Scanner(System.in);
				//输出提示信息
				switch(select)
				{case 2:	
					System.out.println("请输入书号：");
					break;
				case 3:
					System.out.println("请输入书名：");
					break;
				case 4:
					System.out.println("请输入作者：");
					break;
				case 5:
					System.out.println("请输入出版社：");
					break;
				case 6:
					System.out.println("请输入种类：");
					break;
				}	
				
				String req=reader.nextLine(); 	
				String sql="select *\r\n"+ 
						"from book\r\n" ;
				
			    //编辑sql语句
				//根据不同的choice添加不同的sql语句
				switch(select)
				{
				case 2:sql=sql+"where bno=?;";break;
				case 3:sql=sql+"where title=?;";break;
				case 4:sql=sql+"where author=?;";break;
				case 5:sql=sql+"where press=?;";break;
				case 6:sql=sql+"where category=?;";break;
				}
				
				//设置参数并查询
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, req);
				ResultSet res = ps.executeQuery();
				
				//输出查询结果
				Book_Query.print_Book(res);
				
				ps.close();
			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//区间查询
		static void section_Book(Connection conn,int select) throws SQLException
		{
			try {
				Scanner reader=new Scanner(System.in);
				String left="";
				String right="";
				String sql="select *\r\n"+ 
						"from book\r\n" ;
				//查询年份
				if(select==7)
				{
					System.out.println("请输入查询年份上/下限（空格分割）：");
					left=reader.next();
					right=reader.next();
					sql+="where year>=?&&year<=?;";//设置sql语句
				}
				//查询价格
				else if(select==8)
				{
					System.out.println("请输入查询价格上/下限：");
					left=reader.next();
					right=reader.next();
					sql+="where price>=?&&price<=?;";//设置sql语句
				}
				//设置参数并查询
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, left);
				ps.setString(2, right);
				ResultSet res = ps.executeQuery();
				//输出查询结果
				Book_Query.print_Book(res);
				
				ps.close();
			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//主键查询
		static ResultSet key_find(Connection conn,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from book\r\n" + 
					"where bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			//设置参数
			ps.setString(1, bno);
			ResultSet res= ps.executeQuery();
			//返回查询结果
			return res;
		}
		//格式化输出book表中信息
		static void print_Book(ResultSet rset) throws SQLException
		{
			System.out.println("查询结果如下");
		    System.out.println("********************************************************************************");
			System.out.println("书号\t书名\t作者\t出版社\t种类\t年份\t价格\t库存");
			System.out.println("********************************************************************************");
			
			while (rset.next()) { 
				System.out.println(rset.getString("bno")+"\t"+rset.getString("title")+
						"\t"+rset.getString("author")+"\t"+rset.getString("press")+
						"\t"+rset.getString("category")+"\t"+rset.getInt("year")+
						"\t"+rset.getDouble("price")+"\t"+rset.getInt("stock"));}
		}
}
