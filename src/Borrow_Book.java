import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Borrow_Book {
	//借书功能主菜单
	static void borrow_Book(Connection conn) throws SQLException
	{
		Statement stmt=conn.createStatement();
		Scanner reader=new Scanner(System.in);
		
		System.out.println("请输入书号,借阅证编号(0返回上级菜单)");
		//读取信息为字符串
		String str=reader.nextLine();
        //如果输入0，则返回上级菜单
		if(str.contentEquals("0"))return;
		//分割字符串
		String[] s = str.split("\\,");
		String bno=s[0];
		String cno=s[1];
        
		//从表中查询信息，用以判断当前是哪种情况
		ResultSet res= Book_Query.key_find(conn,bno);
		ResultSet res1=Borrow.key_find(conn,cno,bno);
		ResultSet res2=Borrow.key_findbno(conn,bno);
		ResultSet res3=Proof.key_find(conn,cno);
		
		//如果book表中没有该编号
		if (!res.next()) {
		    System.out.println("图书库中没有编号为输入信息的图书！");	
		} 
		//book表中有该书信息，但库存量为0
		else if(res.getInt("stock")==0){
			System.out.println("该书的库中库存为0！");//输出提示信息
			//如果borrow表中有该书信息
			if(res2.next())
			{
				//输出该书所有的借阅信息
				Borrow.print_Borrow(res2);	
			}
		}
		//如果库中没有改编号的借书卡
		else if(!res3.next())
		{
			System.out.println("图书库中没有编号为输入信息的借书证！");	
		}
		//如果borrow表钟并无该条借阅信息
		else if(!res1.next()){
			try {
				//保持原子性
				
				//改变book中该书库存量
				String sql = "update book"
					+ " set stock=stock-1"
					+ " where bno=?";		
				PreparedStatement ps;
				ps = conn.prepareStatement(sql);

				ps.setString(1, bno);
				
				ps.executeUpdate();
				
				//增加信息到borrow
				sql="insert into borrow values"
						+ "(?,?,?,?);";
				
				ps=conn.prepareStatement(sql);
				//设置参数
				ps.setString(1, cno);
				ps.setString(2, bno);
				java.util.Date date=new java.util.Date();
				java.sql.Date date1=new java.sql.Date(date.getTime());			
				ps.setDate(3, date1);
				ps.setNull(4, java.sql.Types.DATE);

				ps.executeUpdate();
				//提交
				conn.commit();
				System.out.println("成功借书！(*^▽^*)");
			}
			catch (Exception sqle){	
				//回滚
				conn.rollback();
				System.out.println("借书失败！(ㄒoㄒ) ");
				System.out.println("Exception : " + sqle);
			}	
		}	
		else//如果borrow表中有该条信息
		{
			//如果借阅信息中并未归还
			if(res1.getString(4)==null)
			{
				System.out.println("卡号"+cno+"书号"+bno+"该卡已借阅过该书！并且还未归还！╰（‵□′）╯ ");	
			}
			else//如果已经借阅并归还
			{
				System.out.println("卡号"+cno+"书号"+bno+"该卡已借阅过该书！请问是否再次借阅? 1.是 0.否，返回上级菜单");
				int choice;
				choice=reader.nextInt();
				if(choice==1)
				{
					//再次借阅该书
					try {
					//修改库存量
					String sql = "update book\r\n"
							+ " set stock=stock-1\r\n"
							+ " where bno=?";		
					PreparedStatement ps;
					ps = conn.prepareStatement(sql);
					//设置参数
					ps.setString(1, bno);	
					
					ps.executeUpdate();
					
					//更新borrow中的信息
					sql="update borrow\r\n" + 
							"set borrow_date=?,return_date=?\r\n" + 
							"where cno=?&& bno=?";
					
					ps=conn.prepareStatement(sql);
					
					//设置参数			
					java.util.Date date=new java.util.Date();
					java.sql.Date date1=new java.sql.Date(date.getTime());
					
					ps.setDate(1, date1);
					ps.setNull(2, java.sql.Types.DATE);				
					ps.setString(3, cno);
					ps.setString(4, bno);

					ps.executeUpdate();
					//提交
					conn.commit();
					System.out.println("成功借书！(*^▽^*)");
					}catch (Exception sqle){	
						//回滚
						conn.rollback();
						System.out.println("借书失败！(ㄒoㄒ) ");
						System.out.println("Exception : " + sqle);
					}
				}	
				
			}
		}
		stmt.close();
	}
	
	
}
