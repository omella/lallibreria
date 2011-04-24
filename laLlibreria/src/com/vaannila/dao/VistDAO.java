package com.vaannila.dao;

import java.util.List;

import com.vaannila.domain.QueryPair;
import com.vaannila.domain.Vist;

public interface VistDAO {

	public void saveVist(Vist vist);

	public List<QueryPair> getVistList();
}
