package org.vardb.setup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.vardb.util.CMessageWriter;

public class CDatabaseImporter
{
	private final JdbcTemplate jdbcTemplate;
	private final CMessageWriter writer;
	
	public CDatabaseImporter(DataSource datasource, CMessageWriter writer)
	{		
		this.jdbcTemplate=new JdbcTemplate(datasource);
		this.writer=writer;
	}
	
	/*
	public Collection<CUser> getUsers()
	{		
		StringBuilder buffer=new StringBuilder();
		buffer.append("SELECT *\n");
		buffer.append("FROM users\n");
		buffer.append("WHERE anonymous=false\n");
		buffer.append("AND enabled=true\n");
		UserMapper mapper=new UserMapper();
		return jdbcTemplate.query(buffer.toString(),mapper);
	}
	
	public class UserMapper implements RowMapper<CUser>
	{
		public CUser mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			CUser user=new CUser();
			user.setId(rs.getString("id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setEnabled(true);
			user.setAnonymous(false);
			//user.setRoot(rs.getBoolean("root"));
			user.setAdministrator(rs.getBoolean("administrator"));
			//user.setUsedata(rs.getBoolean("usedata"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			user.setEmail(rs.getString("email"));
			user.setAffiliation(rs.getString("affiliation"));
			user.setCreated(rs.getDate("created"));
			user.setUpdated(rs.getDate("updated"));
			writer.message("importing user="+user.getUsername());
			return user;
	    }
	}
	*/
}