import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Proof {
	//�������֤�����˵�
	static void proof_Manag(Connection conn) throws SQLException, FileNotFoundException
	{
		Statement stmt=conn.createStatement();
		Scanner reader=new Scanner(System.in);
		
		while(true){
		System.out.println("1.��ѯ�������н���֤ 2.�������ӽ���֤ 3.���ļ������������֤ 4.����֤�޸� 5.ɾ������֤ 0.�����ϼ��˵�");
		System.out.println("��������Ҫ�ķ�����:");
		int choice=reader.nextInt();
		switch(choice)
		{
		    case 0 :
				return;
		    case 1:
		    	try {//��ѯ���н���֤
		    	    String query="select * from card";
				    stmt = conn.createStatement();
				    ResultSet rset = stmt.executeQuery(query);
				    //�����ѯ���
				    Proof.PrintCard(rset);
				    stmt.close();
		    	}
		    	catch (Exception sqle){//�����쳣��Ϣ
					System.out.println("Exception : " + sqle);
				}	
				break;
		    case 2 :
		    	try {//���ӽ���֤
		    	System.out.println("��������Ҫ���ӵĽ���֤��Ϣ����ʽ���,����,Ժϵ,����(T/S)��:");
		    	reader.nextLine();//��ȡ����س�
		    	//��ȡ��ϢΪ�ַ���
				String str=reader.nextLine();
				//�ָ��ַ���
				String[] s = str.split("\\,");
				String cno = s[0];
				String name = s[1];
				String department = s[2];
				String type = s[3];
			
				//������Ϣ��card
				String sql="insert into card values"
						+ "(?,?,?,?)";
					
					PreparedStatement ps=conn.prepareStatement(sql);
					//���ò���
					ps.setString(1, cno);
					if(name=="") ps.setString(2, null);
					else ps.setString(2, name);
					if(department=="") ps.setString(3, null);
					else ps.setString(3, department);
					if(type=="") ps.setString(4, null);
					else ps.setString(4, type);


					ps.executeUpdate();
					//�ύ
					conn.commit();
				    System.out.println("�ɹ����ӽ���֤��");//���������Ϣ
		    	}catch (Exception sqle){//�����쳣��Ϣ
		    		//�ع�
		    		conn.rollback();
					System.out.println("δ�ܳɹ�ɾ����");//���������Ϣ
					System.out.println("Exception : " + sqle);
				}
				break;
		    case 3:
		    	//�����ļ���
		    	System.out.println("�������ļ�����");
		    	reader.nextLine();//��ȡ����س�
		    	String card=reader.nextLine();
		    	//���ļ����������֤��Ϣ
		    	Proof.file_addcard(conn,card);
		    	break;
			case 4://�������еĽ���֤��Ϣ
				update_card(conn);
				break;
			case 5:
				try {
					//ɾ�����н���֤
				System.out.println("��������Ҫɾ���Ľ���֤���:");
				reader.nextLine();//��ȡ����س�
				String cno=reader.nextLine();
				String sql;
				//ɾ����Ϣ
				sql="delete from card\r\n"
						+ "where cno=?";
				PreparedStatement ps;
					
				ps=conn.prepareStatement(sql);
					//���ò���
				ps.setString(1, cno);
				ps.executeUpdate();
				//�ύ
				conn.commit();
				System.out.println("�ɹ�ɾ������֤��");
				}catch (Exception sqle){
					//�ع�
					conn.rollback();
					System.out.println("δ�ܳɹ�ɾ��");
					System.out.println("Exception : " + sqle);
				}
				break;
			default:
				System.out.println("�����Ŵ���~(T_T)~");
		}
		}
	}
	//�������еĽ���֤��Ϣ
	static void update_card(Connection conn) throws SQLException	
	{
		try {
			Scanner reader=new Scanner(System.in);
			int choice;
			System.out.println("��������Ҫ���µĽ���֤���(0�˳�):");

			String cno=reader.nextLine();
			System.out.println("��ǰ����֤���"+cno+"��ѡ��1.���ĳ������ȫ����Ϣ 2.�������� 3.����Ժϵ 4.�������� 0.�˳���������һ���˵�:");
			choice=reader.nextInt();//��ȡ����ѡ����
			
			String sql="update card\r\n";
			PreparedStatement ps;
			if(choice==1)
			{
				//����������Ϣ
				sql="update card\r\n"
						+ "set name=?,department=?,type=?\r\n"
						+ "where cno=?;\r\n";
				System.out.println("����������,Ժϵ,���ͣ�T/S��:");
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
				//�����Ĳ�����Ϣ
				switch(choice)//���ݱ�ű༭��ͬ��sql���
				{
				case 0:return;
				case 2:
					sql+="set name=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("�������µ�������Ϣ��");
					break;
				case 3:
					sql+="set department=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("�������µ�Ժϵ��Ϣ��");
					break;
				case 4:
					sql+="set type=?\r\n"
							+ "where cno=?;\r\n";
					System.out.println("�������µ�������Ϣ��T/S����");
					break;				
				}
				reader.nextLine();//��ȡ����س�
				String info =reader.nextLine();
				ps=conn.prepareStatement(sql);
				//���ò���
				ps.setString(1,info);
				ps.setString(2,cno);				
			}
			ps.executeUpdate();
			//�ύ
			conn.commit();
			System.out.println("�ɹ��޸���Ϣ��");		
		}catch (Exception sqle){
			//�ع�
			conn.rollback();
			System.out.println("δ�ܳɹ��޸���Ϣ��");
			System.out.println("Exception : " + sqle);
		}	
	}
	//���ļ��������֤��Ϣ
	static void file_addcard(Connection conn,String filename) throws FileNotFoundException, SQLException
	{
		File InputFile = new File(filename); //����file���ȡ�ļ�
		if(InputFile.exists())
		{
			Scanner input = new Scanner(InputFile); //��file�Ķ�������һ��scanner����
            
			while(input.hasNext()){
				String cno= input.next();
				String name = input.next();
				String department = input.next();
				String type = input.next();
				
				if(Proof.key_find(conn,cno).next())//�������иý���֤
				{
					System.out.println("���"+cno+"�������иý���֤��");
				}
				else{
					try {
						//������Ϣ��card
						String sql="insert into card values"
								+ "(?,?,?,?)";
						PreparedStatement ps;
							
						ps=conn.prepareStatement(sql);
						
						//���ò���
						ps.setString(1, cno);
						
						if(name.equals("null")) ps.setString(2, null);//nullʵ�ֿ�ֵ����
						else ps.setString(2, name);
						if(department.equals("null")) ps.setString(3, null);
						else ps.setString(3, department);
						if(type.equals("null")) ps.setString(4, null);
						else ps.setString(4,type);			
						
						ps.executeUpdate();
						//�ύ
						conn.commit();
						}catch (Exception sqle){
							//�ع�
							conn.rollback();
							System.out.println("���"+cno+"δ�ܳɹ�������У����n��");	
							System.out.println("Exception : " + sqle);
						}		 
				}		
			}
			System.out.println("�ɹ������ļ��е�ȫ������֤��Ϣ��\\(�R���Q*)o");
	    	input.close();
		}
		else
		{
			System.out.println("�Ҳ������ļ������n��");	
		}
	}
	//�������ң������жϱ����Ƿ���ĳ����Ϣ
	static ResultSet key_find(Connection conn,String cno) throws SQLException
	{
		String sql="select *\r\n"+ 
				"from card\r\n" + 
				"where cno=?;";
		
		PreparedStatement ps = conn.prepareStatement(sql);
		//���ò���
		ps.setString(1, cno);
		ResultSet res= ps.executeQuery();
		//���ز�ѯ���
		return res;
	}
	//��ʽ�������ѯ���
	static void PrintCard(ResultSet rset) throws SQLException
	{
		System.out.println("��ѯ�������");
	    System.out.println("********************************************************************************");
		System.out.println("����֤��\t���\tԺϵ\t����");
		System.out.println("********************************************************************************");
		
		while (rset.next()) { 
			System.out.println(rset.getString("cno")+"\t"+rset.getString("name")+
					"\t"+rset.getString("department")+"\t"+rset.getString("type"));
		}	
	}
}
