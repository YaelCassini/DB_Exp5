import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Proof {
	//管理借书证的主菜单
	static void proof_Manag(Connection conn) throws SQLException, FileNotFoundException
	{
		Statement stmt=conn.createStatement();
		Scanner reader=new Scanner(System.in);
		
		while(true){
		System.out.println("1.查询库中所有借书证 2.单行增加借阅证 3.从文件批量导入借书证 4.借阅证修改 5.删除借阅证 0.返回上级菜单");
		System.out.println("请输入需要的服务编号:");
		int choice=reader.nextInt();
		switch(choice)
		{
		    case 0 :
				return;
		    case 1:
		    	try {//查询所有借书证
		    	    String query="select * from card";
				    stmt = conn.createStatement();
				    ResultSet rset = stmt.executeQuery(query);
				    //输出查询结果
				    Proof.PrintCard(rset);
				    stmt.close();
		    	}
		    	catch (Exception sqle){//处理异常信息
					System.out.println("Exception : " + sqle);
				}	
				break;
		    case 2 :
		    	try {//增加借书证
		    	System.out.println("请输入需要增加的借阅证信息（格式编号,姓名,院系,类型(T/S)）:");
		    	reader.nextLine();//读取多余回车
		    	//读取信息为字符串
				String str=reader.nextLine();
				//分割字符串
				String[] s = str.split("\\,");
				String cno = s[0];
				String name = s[1];
				String department = s[2];
				String type = s[3];
			
				//增加信息到card
				String sql="insert into card values"
						+ "(?,?,?,?)";
					
					PreparedStatement ps=conn.prepareStatement(sql);
					//设置参数
					ps.setString(1, cno);
					if(name=="") ps.setString(2, null);
					else ps.setString(2, name);
					if(department=="") ps.setString(3, null);
					else ps.setString(3, department);
					if(type=="") ps.setString(4, null);
					else ps.setString(4, type);


					ps.executeUpdate();
					//提交
					conn.commit();
				    System.out.println("成功增加借阅证！");//输出反馈信息
		    	}catch (Exception sqle){//处理异常信息
		    		//回滚
		    		conn.rollback();
					System.out.println("未能成功删除！");//输出反馈信息
					System.out.println("Exception : " + sqle);
				}
				break;
		    case 3:
		    	//输入文件名
		    	System.out.println("请输入文件名：");
		    	reader.nextLine();//读取多余回车
		    	String card=reader.nextLine();
		    	//从文件中载入借书证信息
		    	Proof.file_addcard(conn,card);
		    	break;
			case 4://更新已有的借书证信息
				update_card(conn);
				break;
			case 5:
				try {
					//删除已有借书证
				System.out.println("请输入需要删除的借阅证编号:");
				reader.nextLine();//读取多余回车
				String cno=reader.nextLine();
				String sql;
				//删除信息
				sql="delete from card\r\n"
						+ "where cno=?";
				PreparedStatement ps;
					
				ps=conn.prepareStatement(sql);
					//设置参数
				ps.setString(1, cno);
				ps.executeUpdate();
				//提交
				conn.commit();
				System.out.println("成功删除借书证！");
				}catch (Exception sqle){
					//回滚
					conn.rollback();
					System.out.println("未能成功删除");
					System.out.println("Exception : " + sqle);
				}
				break;
			default:
				System.out.println("服务编号错误~(T_T)~");
		}
		}
	}
	//更新已有的借书证信息
	static void update_card(Connection conn) throws SQLException	
	{
		try {
			Scanner reader=new Scanner(System.in);
			int choice;
			System.out.println("请输入需要更新的借阅证编号(0退出):");

			String cno=reader.nextLine();
			System.out.println("当前借阅证编号"+cno+"请选择：1.更改除编号外全部信息 2.更改姓名 3.更改院系 4.更改类型 0.退出，返回上一级菜单:");
			choice=reader.nextInt();//读取功能选择编号
			
			String sql="update card\r\n";
			PreparedStatement ps;
			if(choice==1)
			{
				//更改所有信息
				sql="update card\r\n"
						+ "set name=?,department=?,type=?\r\n"
						+ "where cno=?;\r\n";
				System.out.println("请输入姓名,院系,类型（T/S）:");
				reader.nextLine();
				String str=reader.nextLine();
				String[] s = str.split("\\,");
				String name=s[0];
				String department=s[1];
				String type=s[2];
				
				ps=conn.prepareStatement(sql);
				
				if(name=="") ps.setString(1, null);
				else ps.setString(1, name);
				if(department=="") ps.setString(2, null);
				else ps.setString(2, department);
				if(type=="") ps.setString(3, null);
				else ps.setString(3, type);
				ps.setString(4, cno);
				
			}
			else 
			{
				//仅更改部分信息
				switch(choice)//根据编号编辑不同的sql语句
				{
				case 0:return;
				case 2:
					sql+="set name=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("请输入新的姓名信息：");
					break;
				case 3:
					sql+="set department=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("请输入新的院系信息：");
					break;
				case 4:
					sql+="set type=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("请输入新的类型信息（T/S）：");
					break;				
				}
				reader.nextLine();//读取多余回车
				String info =reader.nextLine();
				ps=conn.prepareStatement(sql);
				//设置参数
				ps.setString(1,info);
				ps.setString(2,cno);				
			}
			ps.executeUpdate();
			//提交
			conn.commit();
			System.out.println("成功修改信息！");		
		}catch (Exception sqle){
			//回滚
			conn.rollback();
			System.out.println("未能成功修改信息！");
			System.out.println("Exception : " + sqle);
		}	
	}
	//从文件加入借书证信息
	static void file_addcard(Connection conn,String filename) throws FileNotFoundException, SQLException
	{
		File InputFile = new File(filename); //先用file类读取文件
		if(InputFile.exists())
		{
			Scanner input = new Scanner(InputFile); //用file的对象生成一个scanner对象
            
			while(input.hasNext()){
				String cno= input.next();
				String name = input.next();
				String department = input.next();
				String type = input.next();
				
				if(Proof.key_find(conn,cno).next())//库中已有该借书证
				{
					System.out.println("编号"+cno+"库中已有该借书证！");
				}
				else{
					try {
						//增加信息到card
						String sql="insert into card values"
								+ "(?,?,?,?)";
						PreparedStatement ps;
							
						ps=conn.prepareStatement(sql);
						
						//设置参数
						ps.setString(1, cno);
						
						if(name.equals("null")) ps.setString(2, null);//null实现空值输入
						else ps.setString(2, name);
						if(department.equals("null")) ps.setString(3, null);
						else ps.setString(3, department);
						if(type.equals("null")) ps.setString(4, null);
						else ps.setString(4,type);			
						
						ps.executeUpdate();
						//提交
						conn.commit();
						}catch (Exception sqle){
							//回滚
							conn.rollback();
							System.out.println("编号"+cno+"未能成功加入库中！＞﹏＜");	
							System.out.println("Exception : " + sqle);
						}		 
				}		
			}
			System.out.println("成功处理文件中的全部借书证信息！\\(≧▽≦*)o");
	    	input.close();
		}
		else
		{
			System.out.println("找不到该文件！＞﹏＜");	
		}
	}
	//主键查找，用于判断表中是否有某个信息
	static ResultSet key_find(Connection conn,String cno) throws SQLException
	{
		String sql="select *\r\n"+ 
				"from card\r\n" + 
				"where cno=?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		//设置参数
		ps.setString(1, cno);
		ResultSet res= ps.executeQuery();
		//返回查询结果
		return res;
	}
	//格式化输出查询结果
	static void PrintCard(ResultSet rset) throws SQLException
	{
		System.out.println("查询结果如下");
	    System.out.println("********************************************************************************");
		System.out.println("借书证号\t书号\t院系\t类型");
		System.out.println("********************************************************************************");
		
		while (rset.next()) { 
			System.out.println(rset.getString("cno")+"\t"+rset.getString("name")+
					"\t"+rset.getString("department")+"\t"+rset.getString("type"));
		}	
	}
}
