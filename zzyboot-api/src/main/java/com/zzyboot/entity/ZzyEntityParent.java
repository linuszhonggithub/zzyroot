package com.zzyboot.entity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.zzyboot.api.util.ZzyUtil;
import com.zzyboot.common.util.ZzyCommon;

import lombok.Data;

@Data
@MappedSuperclass
public class ZzyEntityParent implements Cloneable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(updatable = false, nullable = false)
	@ZzyColumn(fieldtype = "hidden",seq=100000)
	protected Long id;
	
	@Version
	@Column
	@ZzyColumn(fieldtype = "hidden",seq=90000)
	protected Long zzyoptlock = 0L;
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZzyEntityParent other = (ZzyEntityParent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	@Column(length = 50)
	@ZzyColumn(fieldtype = "text",seq=90, editright = "readonly")
	protected String entryid;
	
	@Column
	@ZzyColumn(fieldtype = "datetime",seq=80, editright = "readonly")
	protected Long entrytime;
	
	public ZzyTables getTableDef(){
		String tablename = this.getClass().getName();
		return ZzyUtil.tableTables.get(tablename);
	}
	public List<ZzyColumns> getColumnDef(){
		String tablename = this.getClass().getName();
		if(ZzyUtil.tableColumns.containsKey(tablename)){
			return ZzyUtil.tableColumns.get(tablename);
		}
		List<ZzyColumns> thisColumn = new ArrayList<ZzyColumns>();
		ZzyTables zt = new ZzyTables(null,null,null,"",null);
		System.out.println("getColumnDef begin");
		Annotation[] aList = this.getClass().getDeclaredAnnotations();
		if(aList!=null){
			for(int i = 0; i < aList.length; i++){
				Annotation ai = aList[i];
				String aiS = ai.toString();
				if(aiS.indexOf("com.zzyboot.entity.ZzyTable")>=0){
					aiS = aiS.substring(aiS.indexOf("(") + 1, aiS.lastIndexOf(")"));
					String[] aiSA = aiS.split(",");
					
					for(int j = 0; j < aiSA.length; j++){
						String aj = aiSA[j];
						if(aj.indexOf("=")<1){
							continue;
						}
						String[] ajA = aj.split("=");
						String name = ajA[0].trim();
						String value = "";
						if(ajA.length > 1){
							value = ajA[1].trim();
						}
						if(name.equals("showright")){
							zt.setShowright(value);
						}else if(name.equals("editright")){
							zt.setEditright(value);
						}else if(name.equals("insertright")){
							zt.setInsertright(value);
						}else if(name.equals("deleteright")){
							zt.setDeleteright(value);
						}else if(name.equals("label")){
							zt.setLabel(value);
						}
						
					}
					
					break;
				}
			}
		}
		ZzyUtil.tableTables.put(tablename, zt);
		if(this.getClass().getSuperclass()!=null){
			Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
			//Field[] fields = this.getClass().getFields();	
			thisColumn = getColumnDef(fields);
		}
		Field[] fields = this.getClass().getDeclaredFields();
		
		thisColumn.addAll(getColumnDef(fields));
		thisColumn.sort(Comparator.comparingInt(ZzyColumns::getSeq).reversed());
		ZzyUtil.tableColumns.put(tablename,thisColumn);
		return thisColumn;
	}
	public List<ZzyColumns> getColumnDef(Field[] fields){
		//if(thisColumn!=null)return thisColumn;
		List<ZzyColumns> thisColumn = new ArrayList<ZzyColumns>();
		//System.out.println("getColumnDef begin");
		//System.out.println("fields cnt is " + fields.length);
		
		for(int i = 0 ; i < fields.length; i++){
			Field fi = fields[i];
			
			ZzyColumns zc = null;	
			Annotation[] annotations = fi.getDeclaredAnnotations();
			for(int j = 0; j < annotations.length; j++){
				Annotation aj = annotations[j];
				//System.out.println("annotations " + j + " is " + aj.toString() + " for " + fi.getName());
				
				//if(aj.getClass().equals(ZzyColumn.class)){
				if(aj.annotationType().equals(ZzyColumn.class)){
					if(zc == null){
						zc = new ZzyColumns(fi.getName(), "" ,"",false,false,false,false,false,0,100, fi);
						//System.out.println("f ajj" + i + " is " + fi.getName());
					}
					ZzyColumn ajj = (ZzyColumn)aj;
					if(ajj.showright()!=null){
						zc.setShowright(ajj.showright());
					}

					if(ajj.label() != null)zc.setLabel(ajj.label());
					if(ajj.iskeyword())zc.setIsKeyword(true);
					if(ajj.isprimkey())zc.setIsPrimary(true);
					if(ajj.minlength() != 0)zc.setMinLength(ajj.minlength());
					if(ajj.seq() != 0)zc.setSeq(ajj.seq());
					if(ajj.fieldtype() != null)zc.setFieldtype(ajj.fieldtype());
					if(ajj.editright() != null)zc.setEditright(ajj.editright());
					if(ajj.isfrozen())zc.setIsFrozen(true);
				}else if(aj.annotationType().equals(Column.class)){
					if(zc == null){
						/*if(fi.getName().equals("zzyoptlock")){
							continue;
						}*/
						zc = new ZzyColumns(fi.getName(), "" ,"" ,false,false,false,false,false,0,100,fi);
						//System.out.println("f cj" + i + " is " + fi);
					}
					Column cj = (Column)aj;
					if(cj.unique())zc.setIsUnique(true);
					if(!cj.nullable())zc.setIsRequired(true);
				}

			}
			if(zc != null)thisColumn.add(zc);
			
		}
		
		return thisColumn;
	}
	public String getDBList(List<ZzyEntityParent> list){
		StringBuilder sb = new StringBuilder();
		for(ZzyEntityParent z: list){
			sb.append(z.toString() + ZzyCommon.STRSEPLINE);
		}
		return sb.toString();
	}
	public void setValues(String param){
		String[] s = param.split(ZzyCommon.STRSEPITEM);
		List<ZzyColumns> fields = getColumnDef();
		setValues(fields,s);
	}
	private void setField(Field fi, ZzyEntityParent z, String si){
		try {
			if(si == null || si.length() < 1 || si.equals("null")){
				fi.set(z, null);
				return;
			}
			fi.setAccessible(true);
			if(fi.getType().equals(Boolean.class)){
				fi.set(z, Boolean.valueOf(si));	
			}else if(fi.getType().equals(Integer.class)){
				fi.set(z,new Integer(si));	
			}else if(fi.getType().equals(Long.class)){
				fi.set(z, new Long(si));
			}else if(fi.getType().equals(Double.class)){
				fi.set(z, new Double(si));	
			}else if(fi.getType().equals(Date.class)){
				fi.set(z, ZzyUtil.getDate(si));	
			}else{
				fi.set(z, si);
			}
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setValues(List<ZzyColumns> fields,Object[] s){
		setValues(fields,s,null);
	}
	public void setValues(List<ZzyColumns> fields,Object[] s,Set<String> showCols){
		
		int index = 0;
		for(ZzyColumns z: fields){
			if(showCols!=null){
				if(!showCols.contains(z.getName())){
					
					continue;
				}
			}
			Field fi = z.getFi();
			fi.setAccessible(true);
			System.out.println("fi is " +fi);
			String si = null;
			if(index < s.length){
				si = s[index++]+"";
			}
			
			System.out.println(" setValues " + fi.getName()+",values is " + si);
			setField(fi,this,si);
		}
	}
	public String toStringZzy(){
		return toStringZzy(null);
	}
	public String toStringZzy(Set<String> colSet){
		StringBuilder sb = new StringBuilder();
		List<ZzyColumns> fields = getColumnDef();
		for(ZzyColumns z: fields){
			if(colSet != null){
				if(!colSet.contains(z.getName())){
					continue;
				}
			}
			Field fi = z.getFi();
			try {
				fi.setAccessible(true);
				sb.append(fi.get(this)+ZzyCommon.STRSEPITEM);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}
	public ZzyEntityParent newObj(ZzyEntityParent zOld, Object[] obj,List<ZzyColumns> listColumn){
		return newObj(zOld, obj,listColumn,null);
	}
	public ZzyEntityParent newObj(ZzyEntityParent zOld, Object[] obj,List<ZzyColumns> listColumn, Set<String> showCols){
		ZzyEntityParent zNew = null;
		try {
			zNew = (ZzyEntityParent)zOld.clone();
			zNew.setValues(listColumn, obj,showCols);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return zNew;
	}
	public String getTableName(){
		String table=this.getClass().getName();
		String[] tableA = table.split("\\.");
		table = tableA[tableA.length - 1];
		return table;
	}
	public String getWhereUniq(){
		List<ZzyColumns> listColumn = this.getColumnDef();
		List<String> whereList = new ArrayList<String>();
		for(ZzyColumns zcol: listColumn){
			if(zcol.getIsUnique()){
				whereList.add(zcol.getName());
			}
		}
		return getWhere(whereList);
	}
	public String getWhere(String[] wheres){
		
		List<String> whereList = Arrays.asList(wheres);
		return getWhere(whereList);
	}
	public String getWhere(List<String> whereList){
		String where="";
		String whereSeg = "";
		int index = 1;
		for(Object s: whereList){
			where += whereSeg + s + "=?" + (index++);
			whereSeg=" and ";
		}
		return where;
	}
	public Object[] getWhereUniqParam(){
		List<Object> listParam = new ArrayList<Object>();
		List<ZzyColumns> listColumn = this.getColumnDef();
		for(ZzyColumns zcol: listColumn){
			if(zcol.getIsUnique()){
				Field fi = zcol.getFi();
				try {
					Object zObj = fi.get(this);
					listParam.add(zObj);
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		Object[] whereparam=listParam.toArray();
		return whereparam;
	}
	public String getValue(String colname){
		List<ZzyColumns> listColumn = this.getColumnDef();
		for(ZzyColumns zcol: listColumn){
			if(zcol.getName().equals(colname)){
				Field fi = zcol.getFi();
				try {
					Object zObj = fi.get(this);
					return zObj + "";
					
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	public String getCols(){
		List<ZzyColumns> listColumn = this.getColumnDef();
		StringBuilder sb = new StringBuilder();
		String strColsSeg = "";
		for(ZzyColumns zcol: listColumn){
			sb.append(strColsSeg + zcol.getName());
			strColsSeg = ",";
		}
		return sb.toString();
	}
	public String getColsDef(){
		List<ZzyColumns> listColumn = this.getColumnDef();
		StringBuilder sb = new StringBuilder();
		String strColsSeg = "";
		for(ZzyColumns zcol: listColumn){
			sb.append(strColsSeg + zcol.toStringZzy());
			strColsSeg = ZzyCommon.STRSEPLINE;
		}
		return sb.toString();
	}
	public void update(String[] updateinfos){
		List<ZzyColumns> listColumn = this.getColumnDef();
		
		for(int i = 1; i < updateinfos.length; i++){
			String ui = updateinfos[i];
			String[] uiA = ui.split(ZzyCommon.STRSEPITEM);
			for(ZzyColumns z: listColumn){
				if(z.getName().equals(uiA[0])){
					setField(z.getFi(),this,uiA[1]);
					break;
				}
			}
		}
	}
}
