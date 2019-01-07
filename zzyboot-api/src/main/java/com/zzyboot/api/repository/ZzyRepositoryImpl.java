package com.zzyboot.api.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.ZzyCommon;
import com.zzyboot.entity.ZzyColumns;
import com.zzyboot.entity.ZzyEntityParent;
import com.zzyboot.entity.ZzyTables;
import com.zzyboot.entity.Zzylog;
import com.zzyboot.pojo.ZzyTableResult;

@Transactional
public class ZzyRepositoryImpl implements ZzyRepositoryCustom {
	@Autowired
	@PersistenceContext
	private EntityManager em; 
	private static Map<String,Long> zzyTABLEDATATime = new ConcurrentHashMap<String, Long>();
	private static Map<String,ZzyTableResult> zzyTABLEDATA = new ConcurrentHashMap<String, ZzyTableResult>();
	final private long oneday = 86400000l;
	@Override
	public String add(ZzyEntityParent z,String username) {
		// TODO Auto-generated method stub
		if(z.getId() != null){
			return ZzyCommon.ZZYFAILADD;
		}
		//check if uniq col exists
		boolean uniqvalid = getUniqValid(z);
		if(!uniqvalid){
			return ZzyCommon.ZZYFAILADDINCOMPLETE;
		}
		//check if it exists
		boolean exists = getUniqExists(z);
		if(exists){
			return ZzyCommon.ZZYFAILADDDUP;
		}
		z.setEntrytime(ZzyCommon.getNow());
		z.setEntryid(username);
		
		z = save(z);
		List<Zzylog> logList = getLog(z,ZzyCommon.ZZYLOG_ADD,username);
		z = saveLog(z, logList);
		if(z!=null && z.getId()!=null){
			return ZzyCommon.ZZYSUCCESSINSERT;	
		}
		return ZzyCommon.ZZYFAILADDFAIL;
		
	}
	@Transactional
	public ZzyEntityParent saveLog(ZzyEntityParent z, List<Zzylog> loglist){
		for(Zzylog zlog: loglist){
			em.persist(zlog);
		}
		em.persist(z);
		return z;
	}
	private Zzylog getLogClass(ZzyEntityParent z){
		String tablename = z.getTableName().toLowerCase();
		String logname = tablename + ZzyCommon.ZzyLogSuffix;
		return (Zzylog)ZzyUtil.getTableNewInstance(logname);
	}
	private List<Zzylog> getLog(ZzyEntityParent z, String updatetype, String username){
		List<Zzylog> zlist = new ArrayList<Zzylog>();
		String tablename = z.getTableName().toLowerCase();
		if(updatetype.equals(ZzyCommon.ZZYLOG_ADD)){ //new
			List<ZzyColumns> fields = z.getColumnDef();
			
			Long lineid = z.getId();
			Long entrytime = ZzyCommon.getNow();
			
			
			for(ZzyColumns f: fields){
				Field fi = f.getFi();
				try {
					fi.setAccessible(true);
					Object value = fi.get(z);
					if(value == null){
						continue;
					}
					String colvalue = value + "";
					if(colvalue.length() < 1){
						continue;
					}
					Zzylog zlog = getLogClass(z);//(Zzylog)ZzyUtil.getTableNewInstance(logname);
					if(zlog == null){
						return null;
					}
					String colname = fi.getName();
					zlog.setAction(updatetype);
					zlog.setLineid(lineid);
					zlog.setColname(colname);
					zlog.setColvalue(colvalue);
					zlog.setColvalueold("");
					zlog.setEntryid(username);
					zlog.setEntrytime(entrytime);
					zlist.add(zlog);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return zlist;
	}
	@Transactional
	public ZzyEntityParent save(ZzyEntityParent z){
		em.persist(z);
		return z;
	}
	private boolean getUniqValid(ZzyEntityParent z) {
		String table=z.getClass().getName();
		String[] tableA = table.split("\\.");
		table = tableA[tableA.length - 1];
		List<ZzyColumns> listColumn = z.getColumnDef();
		for(ZzyColumns zcol: listColumn){
			if(zcol.getIsUnique()){
				Field fi = zcol.getFi();
				try {
					Object zObj = fi.get(z);
					if(zObj == null){
						return false;
					}
					if((zObj+"").trim().length()<1){
						return false;
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
					
			}
		}
		return true;
	}
	private ZzyEntityParent getUniq(ZzyEntityParent z) {
		String table=z.getTableName();
				/*.getClass().getName();
		String[] tableA = table.split("\\.");
		table = tableA[tableA.length - 1];*/
		String where=z.getWhereUniq();
		if(where.length() < 1){
			return null;
		}
		String strCols = z.getCols();
		Object[] whereparam=z.getWhereUniqParam();
		Object resultOriginal = getSingle(strCols,table, where, whereparam);
		if(resultOriginal == null){
			return null;
		}
		Object[] result = (Object[]) resultOriginal;
		if(result!=null && result.length>0){
			List<ZzyColumns> listColumn = z.getColumnDef();
			ZzyEntityParent oNew= z.newObj(z,result,listColumn);
			System.out.print(oNew.toString());
			return oNew;
		}
		return null;
	}
	private boolean getUniqExists(ZzyEntityParent z) {
		ZzyEntityParent result = getUniq(z);
		
		if(result!=null){
			System.out.println("result is " + result);
			return true;
		}
		return false;
	}
	public Long getSingleID(String table, String where, Object[] whereparam) {
		String strSqlCol = "id";
		Object resultOriginal = getSingle(strSqlCol, table,  where, whereparam);
		if(resultOriginal == null){
			return -1l;
		}
		Object[] result=(Object[])resultOriginal;
		System.out.println("result.length is " + result.length);
		if(result !=null && result.length > 0){
			return (Long)result[0];
		}
		return -1l;
	}
	public Object getSingle(String strSqlCol,String table, String where, Object[] whereparam) {
		String strSql = "select " + strSqlCol + " from " + table.toLowerCase();
		if(where.length()>0){
			strSql+=" where " + where;	
		}
		
		Query query = em.createNativeQuery(strSql);
		for(int i = 0; i < whereparam.length; i++){
			query.setParameter(i+1, whereparam[i]);
		}
		Object result = null;
		try{
			List listRtn = query.getResultList();
			if(listRtn.size() > 0){
				result=listRtn.get(0);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	public ZzyTableResult  findAllLog(ZzyTableResult zt,String tablename, long begin, long end){
		String logname = tablename + ZzyCommon.ZzyLogSuffix;
		zt.setTablename(logname);
		Zzylog z = (Zzylog)ZzyUtil.getTableNewInstance(logname);
		if(z == null){
			zt = null;
			return zt;
		}
		Object[] whereparam = new Object[2];
		whereparam[0] = begin;
		whereparam[1] = end;
		List<Zzylog> zResult = getMore(z,z.getCols(),"entrytime>?1 and entrytime<=?2",whereparam );
		if(zResult!=null && zResult.size() > 0){
			//StringBuilder sbData = new StringBuilder();
			//sbData.append(ZzyCommon.STRSEPBLOCK + logname + ZzyCommon.STRSEPLINE);
			List<String> datalist = new ArrayList<String>();
			for(ZzyEntityParent zr: zResult){
				datalist.add(zr.toStringZzy() + ZzyCommon.STRSEPLINE);
						//sbData.append(zr.toStringZzy() + ZzyCommon.STRSEPLINE);
			}
			zt.setDatalist(datalist);
			return zt;
		}
		zt = null;
		return zt;
	}
	public ZzyTableResult findAll(ZzyTableResult zt,ZzyEntityParent z, long begin, long end,String scols, String scolsReadonly,Boolean tableisreadonly,Boolean tablecaninsert,Boolean tablecandelete,String username,Boolean issuper) {
		String tablename = z.getTableName();
		System.out.println("ZzyRepositoryImpl findAll, begin["+begin+"] tablename " + tablename);
		if(begin > 0){ //get from log
			return findAllLog(zt,tablename,begin,end);
		}else{
			//get from cache
			StringBuilder sb = new StringBuilder();	
			if(zzyTABLEDATATime.containsKey(tablename)){  //remove cache if over 1 hour
				long lasttime = zzyTABLEDATATime.get(tablename);
				System.out.println("ZzyRepositoryImpl findAll, with tablename " + tablename+", lasttime + timeout time is " + (lasttime + ZzyUtil.ZZYTIMEOUTTABLEDATA) +", now is " + ZzyCommon.getNow());
				if(lasttime + ZzyUtil.ZZYTIMEOUTTABLEDATA > ZzyCommon.getNow()){
					zzyTABLEDATATime.remove(tablename);
					System.out.println("ZzyRepositoryImpl findAll, remove zzyTABLEDATATime with tablename " + tablename);
					zzyTABLEDATA.remove(tablename);
					return findAll(zt,z, begin, end,scols, scolsReadonly,tableisreadonly,tablecaninsert,tablecandelete,username, issuper);
				}
			}
			if(zzyTABLEDATATime.containsKey(tablename)){   //merge with log
				System.out.println("ZzyRepositoryImpl findAll, get data from zzyTABLEDATATime with tablename " + tablename);
				begin = zzyTABLEDATATime.get(tablename);
				zt.setDatalist(zzyTABLEDATA.get(tablename).getDatalist());
				System.out.println("==== the data list for zzyTableData " + tablename + " is " + zt.getDatalist().size());
				if(zt.getDatalist().size() < 1){
					begin = 0;
				}
				/*sb.append(zzyTABLEDATA.get(tablename));
				sb.append(findAll(zt,z, begin, end,scols, scolsReadonly,tableisreadonly,tablecaninsert,tablecandelete,username,issuper));*/
				zt = findAll(zt,z, begin, end,scols, scolsReadonly,tableisreadonly,tablecaninsert,tablecandelete,username,issuper);
				return zt;
			}
			Object[] whereparam = new Object[1];
			whereparam[0] = end;
			List<ZzyColumns> cList = z.getColumnDef();
			ZzyTables ztable = z.getTableDef();
			zt.setTabledesc(ztable.toStringZzy(tablecaninsert,tableisreadonly,tablecandelete));
			//sb.append(ZzyCommon.STRSEPBLOCK + tablename+ ZzyCommon.STRSEPITEM + ztable.toStringZzy(tablecaninsert,tableisreadonly,tablecandelete)+ZzyCommon.STRSEPLINE);
			String[] scolsA = scols.split(",");
			//System.out.println("scols is  " + scols+", scolsA length is " + scolsA.length);
			
			Set<String> colSet = new HashSet<String>();
			
			
			if(!ZzyUtil.zzyUserCols.containsKey(username)){
				ZzyUtil.zzyUserCols.put(username,new ConcurrentHashMap<String, Set<String>>());
			}
			Map<String, Set<String>> mapTableCols = ZzyUtil.zzyUserCols.get(username);
			String tablenamelow = tablename.toLowerCase();
			if(!mapTableCols.containsKey(tablenamelow)){
				for(int i = 0; i < scolsA.length; i++){
					String si = scolsA[i];
					if(si == null || si.length()<0){
						continue;
					}
					
					colSet.add(si);
					//System.out.println("colSet add " + si);
				}
				mapTableCols.put(tablenamelow, colSet);
				System.out.println("=========mapTableCols put " + tablenamelow + " for " + username);
			}else{
				colSet = mapTableCols.get(tablenamelow); 
			}

			
			Set<String> colSetReadonly = new HashSet<String>();
			if(tableisreadonly){
				scolsReadonly = scols;
			}
			if(scolsReadonly!=null && scolsReadonly.length()>0){
				String[] scolsAReadonly = scolsReadonly.split(",");
				for(int i = 0; i < scolsAReadonly.length; i++){
					String si = scolsAReadonly[i];
					if(si == null || si.length()<0){
						continue;
					}
					
					colSetReadonly.add(si);
					//System.out.println("colSet add " + si);
				}
				
			}
			List<String> headerList = new ArrayList<String>(); 
			for(ZzyColumns zc:cList){
				//System.out.println("zcName is " + zc.getName());
				if(!colSet.contains(zc.getName())){
					continue;
				}
				boolean isreadonly = false;
				if(colSetReadonly.contains(zc.getName())){
					isreadonly = true;
				}
				boolean bissuper = issuper;
				//System.out.println("tablename is " + tablename);
				/*if(tablename.indexOf(ZzyCommon.ZzyLogSuffix) > 0){ //log no need id, zzylock
					bissuper = false;
				}*/
				headerList.add(zc.toStringZzy(isreadonly,bissuper));
				
				//sb.append(ZzyCommon.STRTYPEHEADER + ZzyCommon.STRSEPITEM + zc.toStringZzy(isreadonly,bissuper) + ZzyCommon.STRSEPLINE);
			}
			zt.setHeaderlist(headerList);
			//String[] validRoles = z.getCols();
			List<ZzyEntityParent> zResult = getMore(z,scols,"entrytime<=?1",whereparam );
			List<String> dataList = new ArrayList<String>();
			if(zResult!=null && zResult.size() > 0){
				StringBuilder sbData = new StringBuilder();
				for(ZzyEntityParent zr: zResult){
					dataList.add(zr.toStringZzy(colSet));
						//sbData.append(zr.toStringZzy(colSet) + ZzyCommon.STRSEPLINE);
				}
				//sb.append(sbData.toString());
				if(zzyTABLEDATA.containsKey(tablename)){
					zzyTABLEDATA.remove(tablename);
				}
				zzyTABLEDATA.put(tablename, zt);
				zzyTABLEDATATime.put(tablename, end);
			}
			zt.setDatalist(dataList);
			return zt; //sb.toString();	
		}
		
	}
	
	public List<ZzyEntityParent> findAll(ZzyEntityParent z) {
		// TODO Auto-generated method stub
		return getMore(z);
	}
	public List<ZzyEntityParent> getMore(ZzyEntityParent z) {
		return getMore(z,null);
	}
	public List<ZzyEntityParent> getMore(ZzyEntityParent z,String strSqlCol) {
		return getMore(z,strSqlCol,null,null);
	}
	@SuppressWarnings("unchecked")
	public List getMore(ZzyEntityParent z,String strSqlCol, String where, Object[] whereparam) {
		if(strSqlCol == null || strSqlCol.length() < 1){
			strSqlCol = z.getCols();
		}
		String strSql = "select " + strSqlCol + " from " + z.getTableName().toLowerCase();
		if(where!=null && where.length()>0){
			strSql+=" where " + where;	
		}
		
		//System.out.println(strSql);
		Query query = em.createNativeQuery(strSql);
		if(whereparam!=null){
			for(int i = 0; i < whereparam.length; i++){
				query.setParameter(i+1, whereparam[i]);
			}
		}
		List<ZzyEntityParent> result = new ArrayList<ZzyEntityParent>();
		try{
			List<Object> resultDB = query.getResultList();
			List<ZzyColumns> listColumn = z.getColumnDef();
			System.out.println("resultDB length is " + resultDB.size());
			String[] strSqlColA = strSqlCol.split(",");
			Set<String> showCols = new HashSet<String>();
			for(int i = 0; i < strSqlColA.length; i++){
				String si = strSqlColA[i];
				if(si == null || si.length() < 1){
					continue;
				}
				showCols.add(si);
			}
			for(Object o: resultDB){
				Object[] oA = (Object[]) o;
				ZzyEntityParent znew = z.newObj(z, oA,listColumn,showCols);
				result.add(znew);
			}
		}catch(Exception e){
			//e.printStackTrace();
			return null;
		}
		
		return result;
	}
	@Override
	public String delete(ZzyEntityParent z, String uniqCol,String entryid) {
		String[] uniqCols = uniqCol.split(ZzyCommon.STRSEPITEM);
		String where=z.getWhereUniq();
		String strCols = z.getCols();
		String tablename = z.getTableName();
		Object resultOriginal = getSingle(strCols,tablename, where, uniqCols);
		if(resultOriginal == null){
			return ZzyCommon.ZZYFAILDELETENOTEXISTS;
		}
		Object[] result = (Object[]) resultOriginal;
		if(result!=null && result.length>0){
			List<ZzyColumns> listColumn = z.getColumnDef();
			ZzyEntityParent oNew= z.newObj(z,result,listColumn);
			System.out.print(oNew.toString());
			String logname = tablename + ZzyCommon.ZzyLogSuffix;
			Zzylog zlog = (Zzylog)ZzyUtil.getTableNewInstance(logname);
			if(zlog!=null){
				zlog.setAction("d");
				zlog.setColname("id");
				zlog.setValues(oNew.getId()+"");
				zlog.setEntrytime(ZzyCommon.getNow());
				zlog.setEntryid(entryid);
				
			}
			return delete(oNew,oNew.getId(),zlog);
		}
		return null;
	}
	public String deleteAll(ZzyEntityParent z,String ids,String entryid){
		
		String[] idA = ids.split(ZzyCommon.STREMAIL);
		for(int i = 0; i < idA.length; i++){
			String ii = idA[i];
			if(ii == null || ii.length() < 1){
				continue;
			}
			Zzylog zlog = getLogClass(z);
			zlog.setAction("d");
			Long id = new Long(ii);
			zlog.setColname("id");
			zlog.setLineid(id);
			zlog.setColvalue("");
			zlog.setColvalueold(ii);
			zlog.setEntryid(entryid);
			zlog.setEntrytime(ZzyCommon.getNow());
			delete(z,id,zlog);
		}
		return ZzyCommon.ZZYSUCCESSDELETE + "( " + ids + " )";
	}
	@Transactional
	private String delete(ZzyEntityParent z,Long id,Zzylog zlog){
		if(zlog!=null){
			em.persist(zlog);
		}
		em.remove(em.getReference(z.getClass(),id));
		return ZzyCommon.ZZYSUCCESSDELETE + "(" +id+ ")";
	}
	@Override
	public String update(ZzyEntityParent z, String updateinfo, String entryid) {
		System.out.println("updateinfo is  " + updateinfo);
		String[] updateinfos = updateinfo.split(ZzyCommon.STRSEPLINE);
		String strSql = "select " + z.getCols() + " from " + z.getTableName().toLowerCase() + " where id=?1";	
		
		//System.out.println(strSql);
		Query query = em.createNativeQuery(strSql);
		query.setParameter(1, updateinfos[0]);
		Object resultOriginal = null;
		try{
			resultOriginal = query.getSingleResult();
			if(resultOriginal == null){
				return ZzyCommon.ZZYFAILUPDATENODATA;
			}
			Object[] result = (Object[]) resultOriginal;
			if(result!=null && result.length>0){
				List<ZzyColumns> listColumn = z.getColumnDef();
				ZzyEntityParent oNew= z.newObj(z,result,listColumn);
				List<Zzylog> logList = new ArrayList<Zzylog>();
				for(int i = 1; i < updateinfos.length; i++){
					String ui = updateinfos[i];
					String[] uiA = ui.split(ZzyCommon.STRSEPITEM);
					String sValueOld = oNew.getValue(uiA[0]);
					if(sValueOld == null){
						sValueOld = "";
					}
					Zzylog zlog = getLogClass(z);
					zlog.setAction("u");
					Long id = new Long(updateinfos[0]);
					zlog.setColname(uiA[0]);
					zlog.setLineid(id);
					zlog.setColvalue(uiA[1]);
					zlog.setColvalueold(sValueOld);
					zlog.setEntryid(entryid);
					zlog.setEntrytime(ZzyCommon.getNow());
					logList.add(zlog);
				}
				
				
				oNew.update(updateinfos);
				
				ZzyEntityParent zNew = update(oNew,logList);
				return ZzyCommon.ZZYSUCCESSUPDATE;
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return ZzyCommon.ZZYFAILUPDATENODATA;
		}
		return ZzyCommon.ZZYFAILUPDATENODATA;
	}
	@Transactional
	private ZzyEntityParent update(ZzyEntityParent z, List<Zzylog> loglist){
		em.merge(z);
		for(Zzylog zlog: loglist){
			em.persist(zlog);
		}
		return z;
	}
	@Transactional
	private ZzyEntityParent update(ZzyEntityParent z){
		System.out.println("update " + z.toString());
		em.merge(z);
		return z;
	}
	@Override
	public ZzyEntityParent findOne(ZzyEntityParent z, String cols, String wheres) {
		String[] colA = cols.split(ZzyCommon.STRSEPITEM);
		String[] whereA = wheres.split(ZzyCommon.STRSEPITEM);
		
		Object resultOriginal = getSingle(z.getCols(),z.getTableName(), z.getWhere(colA), whereA);
		if(resultOriginal == null){
			return null;
		}
		Object[] result = (Object[]) resultOriginal;
		if(result!=null && result.length>0){
			List<ZzyColumns> listColumn = z.getColumnDef();
			ZzyEntityParent oNew= z.newObj(z,result,listColumn);
			//System.out.print(oNew.toString());
			return oNew;
		}
		return null;
	}
	public void removeLog(String hostname,String tablename){
		Long endtime = ZzyCommon.getNow() - oneday;
		ZzyTableResult zt = new ZzyTableResult();
		zt.setTablename(tablename);
		ZzyTableResult logs = findAllLog(zt,tablename, 0, endtime);
		
		//return hostname + ZzyCommon.STRSEPBLOCK +tablename+ ZzyCommon.STRSEPBLOCK +endtime+ ZzyCommon.STRSEPBLOCK + logs;
		
	}
	
}
