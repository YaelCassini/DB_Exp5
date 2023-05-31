
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Borrow {
	    //查询借书记录
		static void check_Record(Connection conn) throws SQLException, FileNotFoundException
		{
			Scanner reader=new Scanner(System.in);
			int choice;//存放用户选项		
			
			while(true){
			    System.out.println("1.查询全部 2.按书号查询 3.按借书证编号查询 4.批量导入借阅记录 0.返回上级菜单");
			    System.out.println("请输入需要的服务编号");
			    choice=reader.nextInt();
			    switch(choice)//选择不同的查询方式
				{
				    case 0 ://完成
						return;
					case 1://完成
						try {
							//查询全部
						    String sql="select * from borrow";
						    Statement stmt=conn.createStatement();
						    ResultSet rse = stmt.executeQuery(sql);				
						    Borrow.print_Borrow(rse);		
						    stmt.close();
						}catch (Exception sqle){
							System.out.println("Exception : " + sqle);
						}
						break;  
					case 2:	
						//按卡号查询
						Borrow.cno_Record(conn);
						break;
					case 3:
						//按书号查询
						Borrow.bno_Record(conn);
						break;
					case 4://批量添加借阅信息
						System.out.println("请输入文件名：");
				    	reader.nextLine();
				    	String borrow=reader.nextLine();
						file_addborrow(conn,borrow);
						break;
					default:
						System.out.println("服务编号错误~(T_T)~");
				}					
			}
		}
	
	    //通过借书卡号查询借书记录
		static void cno_Record(Connection conn) throws SQLException
		{
			try {
			    Scanner reader=new Scanner(System.in);	
			    
				System.out.println("请输入需要查询的借书证(0返回上级菜单)：");
				String cno=reader.nextLine();
				if(cno.contentEquals("0"))return;
				else
				{
					ResultSet res=Borrow.key_findcno(conn, cno);  //key查询
					Borrow.print_Borrow(res);	//输出
				}

			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		
		static void bno_Record(Connection conn) throws SQLException//借书证查询借书记录
		{
			
			try {
			    Scanner reader=new Scanner(System.in);	

			    System.out.println("请输入需要查询的借书证(0返回上级菜单)：");
				String bno=reader.nextLine();
				if(bno.contentEquals("0"))return;
				else
				{
					ResultSet res=Borrow.key_findbno(conn, bno);
					Borrow.print_Borrow(res);	
				}

			}catch (Exception sqle){
				System.out.println("Exception : " + sqle);
			}
		}
		//从文件中批量添加
		static void file_addborrow(Connection conn,String filename) throws FileNotFoundException, SQLException
		{
			File InputFile = new File(filename); //先用file类读取文件
			if(InputFile.exists())
			{
				Scanner input = new Scanner(InputFile); //接着用file的对象生成一个scanner对象
	            
				while(input.hasNext()){//按行读取整个文件信息
					String cno= input.next();
					String bno = input.next();
					String borrow_date = input.next();
					String return_date = input.next();
					
					ResultSet record=Borrow.key_find(conn,cno,bno);
					if(record.next())//库中已有该条借书记录
					{
						System.out.println("卡号"+cno+"书号"+bno+"该卡已借阅过该书！╰（‵□′）╯ ");	
						
					}
					else{
						try {
							//增加信息到book
							String sql="insert into borrow values"
									+ "(?,?,?,?)";
							PreparedStatement ps;
								
							ps=conn.prepareStatement(sql);
							
							//设置参数
							ps.setString(1, cno);
							ps.setString(2, bno);
							ps.setString(3, borrow_date);	
							if(return_date.equals("null")) ps.setString(4, null);
							else ps.setString(4, return_date);			
							
							ps.executeUpdate();
							//提交
							conn.commit();
							}catch (Exception sqle){
								//回滚
								conn.rollback();
								System.out.println("卡号"+cno+"书号"+bno+"未能成功加入库中！＞﹏＜");	
								System.out.println("Exception : " + sqle);
							}		 
					}		
				}
				System.out.println("成功处理文件中的全部借阅信息！\\(≧▽≦*)o");
		    	input.close();
			}
			else
			{
				System.out.println("找不到该文件！＞﹏＜");	
			}
		}
		//根据借阅证卡号查询借阅信息
		static ResultSet key_findcno(Connection conn,String cno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where cno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
            //设置参数
			ps.setString(1, cno);
			ResultSet res= ps.executeQuery();
			//返回查询结果
			return res;
		}
		//根据书号查询借阅信息
		static ResultSet key_findbno(Connection conn,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
            //设置参数
			ps.setString(1, bno);
			ResultSet res= ps.executeQuery();
			//返回查询结果
			return res;
		}
		//根据书号和借阅卡号查询借阅信息
		static ResultSet key_find(Connection conn,String cno,String bno) throws SQLException
		{
			String sql="select *\r\n"+ 
					"from borrow\r\n" + 
					"where cno=?&&bno=?;";
			
			PreparedStatement ps = conn.prepareStatement(sql);
			//设置参数
			ps.setString(1, cno);
			ps.setString(2, bno);
			ResultSet res= ps.executeQuery();
			//返回查询结果
			return res;
		}
		//格式化输出borrow表中信息
		static void print_Borrow(ResultSet rset) throws SQLException
		{
			System.out.println("查询结果如下");
		    System.out.println("********************************************************************************");
			System.out.println("借书卡号\t书号\t借书日期\t\t还书日期");
			System.out.println("********************************************************************************");
			
			while (rset.next()) { 
				System.out.println(rset.getString("cno")+"\t"+rset.getString("bno")+
						"\t"+rset.getString("borrow_date")+"\t"+rset.getString("return_date"));
			}
		}
}
