package com.vaannila.dao;


import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.mapping.Array;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hsqldb.lib.Iterator;

import com.googlecode.s2hibernate.struts2.plugin.annotations.SessionTarget;
import com.googlecode.s2hibernate.struts2.plugin.annotations.TransactionTarget;
import com.vaannila.domain.Vist;
import com.vaannila.domain.QueryPair;

public class VistDAOImpl implements VistDAO {

	@SessionTarget
	Session session;
	@TransactionTarget
	Transaction transaction;

	@Override
	public void saveVist(Vist vist){
		try {
			session.save(vist);
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		} 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QueryPair> getVistList() {
		List<QueryPair> result = new ArrayList<QueryPair>();
		try {
			Query query = session.createSQLQuery(
			"SELECT v.ISBN as ISBN, COUNT(v.VIST_ID) AS NUM FROM VIST v group by v.ISBN order by NUM desc");
			List<Object[]> res = query.list();
			
			if(res.size()<10) {
				for(int i=0; i<res.size(); i++) {
					QueryPair p = new QueryPair();
					p.setISBN(res.get(i)[0].toString());
					p.setNUM(res.get(i)[1].toString());
					result.add(p);
				}
			}
			else {	
				for(int i=0; i<10; i++) {
					QueryPair p = new QueryPair();
					p.setISBN(res.get(i)[0].toString());
					p.setNUM(res.get(i)[1].toString());
					result.add(p);
				}
			}
		}
		catch(Exception e){
			System.out.println();
			System.out.println("Error :-(");
		}
		return result;
	}
}
