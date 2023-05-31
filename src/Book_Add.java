import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Book_Add {
	//图书入库主菜单
	static void add_Book(Connection conn) throws SQLException, FileNotFoundException//基本实现   //只有价格是空参数会导致抛出异常
	{
		while(true){
		Scanner reader=new Scanner(System.in);
		int choice;
		System.out.println("1.单行添加 2.从文件添加 0.返回上级菜单");
		System.out.println("请输入需要的服务编号");
		choice=reader.nextInt();
		
		switch(choice)
		{
		    case 0 ://完成
				return;
			case 1:
				try
				{
					reader.nextLine();//读取多余的回车
					while(true)
					{
					System.out.println("请输入需要添加的书号(0退出,返回上一级菜单)");
					String bno=reader.nextLine();
					//如果输入0则返回上一级菜单
					if(bno.contentEquals("0"))break;
					
					//book中查找不到编号为bno的信息
					if(!Book_Query.key_find(conn,bno).next())
					{
						System.out.println("库中还没有该书信息！");
						//加入一条新的图书信息
						add_onebook(conn,bno);
					}
					else
					{
						//已有该书信息，只需增加库存
						add_stock(conn,bno,1);
					}					
				}
				}catch (Exception sqle){
					System.out.println("Exception : " + sqle);
				}
				break;
			case 2:
				//读入文件名
				String filename;
				System.out.println("请输入文件名：");
				reader.nextLine();
				filename=reader.nextLine();
				//从文件中读取图书信息
				file_addbook(conn,filename);
				break;
			default:
				//输出错误信息
				System.out.println("服务编号错误~(T_T)~");				
    	}	
		}
	}
	
	static void add_onebook(Connection conn,String bno) throws SQLException
	{
		Scanner reader=new Scanner(System.in);
		try {
			System.out.println("请输入书名,作者,出版社,种类,年份,价格(0返回上机菜单)");
            //读取图书信息
			String str=reader.nextLine();
			//分割字符串，得到各个属性的信息
			String[] s = str.split("\\,");
			String title = s[0];
			String author = s[1];
			String press = s[2];
			String category = s[3];
			String year=s[4];
			String price=s[5];
			
			//增加信息到book
			String sql="insert into book values"
					+ "(?,?,?,?,?,?,?,?)";
			PreparedStatement ps;				
			ps=conn.prepareStatement(sql);
			
			//设置参数
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
			
			//修改数据库信息
			ps.executeUpdate();
			//提交
			conn.commit();
			System.out.println("已成功向库中添加该书信息！~/(ㄒoㄒ)/~");		
		}catch (Exception sqle){
			//回滚
			conn.rollback();
			System.out.println("未能成功加入该书信息！:( ");	
			System.out.println("Exception : " + sqle);
		}	
	}
	//添加新的图书信息
	static void add_stock(Connection conn,String bno,int number) throws SQLException
	{
		try {
			String sql="update book\r\n"
					+ "set stock=stock+?\r\n"
					+ "where bno=?;\r\n";
			
			PreparedStatement ptmt1 = conn.prepareStatement(sql);
			//设置参数
			ptmt1.setInt(1, number);
			ptmt1.setString(2, bno);
			
			ptmt1.executeUpdate();
			//提交
			conn.commit();		
			System.out.println("编号"+bno+"库中已有该书名录，已成功添加库存！");
			
		}catch (Exception sqle){	
			//回滚
			conn.rollback();
			System.out.println("编号"+bno+"库中已有该书名录，但未成功添加库存！");
			System.out.println("Exception : " + sqle);
		}
	}
	//从文件中添加图书信息
	static void file_addbook(Connection conn,String filename) throws FileNotFoundException, SQLException
	{
		File InputFile = new File(filename); //先用File类读取文件
		if(InputFile.exists())
		{
			Scanner input = new Scanner(InputFile); //用File的对象生成一个scanner对象
            
			while(input.hasNext()){
				//从文件中读取各个参数
				String bno= input.next();
				String title = input.next();
				String author = input.next();
				String press = input.next();
				String category = input.next();
				String year= input.next();
				String price= input.next();
				String stock= input.next();
				
				if(Book_Query.key_find(conn,bno).next())//库中已有该书，只需增加库存
				{
					add_stock(conn,bno,Integer.parseInt(stock));
				}
				else{
					try {
						//增加信息到book
						String sql="insert into book values"
								+ "(?,?,?,?,?,?,?,?)";
						PreparedStatement ps;
							
						ps=conn.prepareStatement(sql);
						
						//设置参数
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
						//提交
						conn.commit();
					}catch (Exception sqle){
						//回滚
						conn.rollback();
						System.out.println("编号"+bno+"未能成功加入库中！＞﹏＜");	
						System.out.println("Exception : " + sqle);
					}		 
				}		
			}
			System.out.println("成功处理文件中的全部图书信息！\\(≧▽≦*)o");
	    	input.close();
		}
		else
		{
			System.out.println("找不到该文件！＞﹏＜");	
		}
	}
}
