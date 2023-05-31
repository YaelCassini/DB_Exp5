import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Return_Book {
	//还书功能主菜单
	static void return_Book(Connection conn) throws SQLException
	{
		Scanner reader=new Scanner(System.in);
		System.out.println("请输入书号,借阅证编号(0返回上级菜单)");
		//读入信息
		String str=reader.nextLine();
		if(str.contentEquals("0"))return;
		//字符串分割
		String[] s = str.split("\\,");
		String bno=s[0];
		String cno=s[1];
		//查询borrow中是否有该条信息
		ResultSet res=Borrow.key_find(conn,cno,bno);

		//borrow表中没有该条信息
		if (!Book_Query.key_find(conn,bno).next()) {
		    System.out.println("库中没有这本图书！请问要将这本书插入图书管理系统吗？1.跳到添加图书菜单，加入该书(不会保留本次还书信息) 0.不需要加入，返回");
		    int choice=reader.nextInt();
		    //用户选择不加入该书信息
		    if(choice==0)return;
		    else Book_Add.add_onebook(conn,bno);//加入该书信息
			
		}
		else {	
			res.next();
			if(res.getString(4)!=null)//如果表中有该条信息且已经归还
			{
				System.out.println("该书已经还回！、(￣▽￣)");
				return;
			}
			try {
				//改变库存量
				String sql = "update book\r\n"
					+ " set stock=stock+1\r\n"
					+ " where bno=?";		//sql语句
				PreparedStatement ps = conn.prepareStatement(sql);

				ps.setString(1, bno);
				
				ps.executeUpdate();
				
				//修改borrow表中信息，再次借阅该书
				sql="update borrow\r\n"
						+ "set return_date=?\r\n"
						+ "where cno=?&&bno=?";
				ps=conn.prepareStatement(sql);
				
				//设置参数
				java.util.Date date=new java.util.Date();
				java.sql.Date date1=new java.sql.Date(date.getTime());
				
				ps.setDate(1, date1);
				ps.setString(2, cno);
				ps.setString(3, bno);

				ps.executeUpdate();
				//提交
				conn.commit();
				System.out.println("成功还书！o(*￣▽￣*)ブ");
			}
			catch (Exception sqle){	
				//回滚
				conn.rollback();
				System.out.println("未能成功还书o(╥﹏╥)o");
				System.out.println("Exception : " + sqle);
			}	
		}
	}
}
