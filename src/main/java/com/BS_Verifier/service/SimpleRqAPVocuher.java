package com.BS_Verifier.service;

import java.sql.SQLException;
import java.util.Date;

import com.deltek.enterprise.application.em.emphrsdat.C.MessageTypes;
import com.deltek.enterprise.system.applicationinterface.ActionInterface;
import com.deltek.enterprise.system.applicationinterface.AppInterface;
import com.deltek.enterprise.system.applicationinterface.DEException;
import com.deltek.enterprise.system.applicationinterface.RSIterator;
import com.deltek.enterprise.system.applicationinterface.ResultSetInterface;
import com.deltek.enterprise.system.applicationinterface.RowSetInterface;
import com.deltek.enterprise.system.applicationinterface.SqlManager;
import com.deltek.enterprise.system.applicationinterface.integration.InvokeLocalGenericWS;
import com.deltek.enterprise.system.serverapi.remoteapi.integration.Message;
import com.deltek.enterprise.system.serverapi.remoteapi.integration.MethodResponseInterface;

public class SimpleRqAPVocuher {
	AppInterface appI = null;
	ResultSetInterface rsI = null;
	RowSetInterface rowI = null;
	ResultSetInterface rsReqLn = null;
	RowSetInterface rowReqLn = null;
	SqlManager sqlMgr = null;
	
	String companyId;
	String userId;
	String vendId;
	String approved = "N";
	String fy_cd;
	String currVendId;
	String prevVendId;
	String vendName;
	String ap1099TaxId;
	String addrDC;
	String line1addr;
	String line2addr;
	String line3addr;
	String cityName;
	String state;
	String postalCode;
	String country;
	String invcId;
	String userName;
	String typeCD1099;
	String currency;
	String ap1099FL;
	String print1099FL;
	String orgId;
	String orgName;
	String acctId;
	String acctName;
	String projId;
	String projName;
	String projAllowCharges;
	String emplId;
	String shipId;
	String emplName;
	String emplEmail;
	String emplTitle;
	String emplOrg;
	String description = null;
	String contentFileName;
	String contentType;
	String contentNotes;

	String machantName;
	String lnDescription;
	String rostIntNotes;


	String req = "N";
	
	Integer currReqId = 0;
	Integer reqId = null;
	Integer lnCount = null;
	Double sumLineRcvdAmt = 0.0;
	Double invcAmt = 0.0;
	Double calcInvcAmt = 0.0;
	Double totInvcAmt = 0.0;
	Double linesTotAmt = 0.0;	
	Double salesTaxRate = 0.0;
	Double 	transSaleTaxAmount =0.0;
	Double rcvdAmt = 0.0;
	Double totRcvdAmt;

	int vchr;
	int pd_no;
	int sub_pd_no;
	int invcFound = 0;
	short attachCount = 0;
	
	Date invcDate;
	Date reqDT;
	Date incurDate;
	
	String fileName = "";
 	String type = "";
 	String notes = "";
	
 	
 	public String sAcctId;
    public String sActiveFl;
    public String sDetlFl;
    public String sAcctName;
    public String sProjReqdFl;
    public String sAcctEntrGrpCd;
    public String sAcctEntrScrCd;
    public String sTopFl;
    public String sAcctTypeCd;

    
	public void createAPVoucher(ActionInterface ai) throws Exception
	{	
		rsI = ai.getResultSet();
		rowI = rsI.getRowSet();
		appI = rsI.getApplication();
		vchr = (rowI.getInteger("XT_VCHR_NO") == null ? 0 : rowI.getInteger("XT_VCHR_NO"));   
		companyId = (String)appI.getConstant("CP_COMPANY_ID");
		
		if(vchr == 0)
		{
			reqId = rowI.getInteger("XT_REQUEST_ID");    
			invcAmt = rowI.getDouble("XT_INVC_AMT") == null ? 0 : rowI.getDouble("XT_INVC_AMT");
			totInvcAmt = rowI.getDouble("TOTAL_EST_REQ_AMT") == null ? 0 : rowI.getDouble("TOTAL_EST_REQ_AMT");
	    	invcDate = (String.valueOf(rowI.getDate("RQ_DT")).equals(null) || String.valueOf(rowI.getDate("RQ_DT")).equals("null") ? null : rowI.getDate("RQ_DT").getTime());
	    	incurDate = (String.valueOf(rowI.getDate("RQ_DT")).equals(null) || String.valueOf(rowI.getDate("RQ_DT")).equals("null") ? null : rowI.getDate("RQ_DT").getTime());
	    	vendId = rowI.getStringValue("VEND_ID");
	    	approved = rowI.getStringValue("XT_APPROVED");
	    	fy_cd = rowI.getStringValue("XT_FY_CD");
	    	pd_no = rowI.getInteger("XT_PD_NO"); 
	    	sub_pd_no = rowI.getInteger("XT_SUB_PD_NO"); 
	    	invcId = rowI.getStringValue("RQ_ID");
	    	addrDC = rowI.getStringValue("ADDR_DC");
	    	cityName = rowI.getStringValue("CITY_NAME");
	    	country= rowI.getStringValue("COUNTRY_CD");
	    	line1addr = rowI.getStringValue("LN_1_ADR");
	    	line2addr = rowI.getStringValue("LN_2_ADR");
	    	line3addr = rowI.getStringValue("LN_3_ADR");
	    	state = rowI.getStringValue("MAIL_STATE_DC");
	    	vendName = rowI.getStringValue("XT_VEND_LONG_NAME");
	    	postalCode = rowI.getStringValue("POSTAL_CD");
	    	currency = rowI.getStringValue("TRN_CRNCY_CD");
	    	ap1099TaxId = rowI.getStringValue("AP_1099_TAX_ID");
	    	typeCD1099 = rowI.getStringValue("XT_S_AP_1099_TYPE_CD");
	    	ap1099FL = (typeCD1099 == null || typeCD1099.equals("") ? "N" : "Y");
	    	salesTaxRate = rowI.getdouble("CST_AMT_PCT_RT");
			transSaleTaxAmount = rowI.getdouble("TRN_SALES_TAX_AMT");
	    	shipId = rowI.getStringValue("XT_SHIP_ID");
			machantName = rowI.getStringValue("MERCHANT_NAME");
			lnDescription = rowI.getStringValue("RQ_LN_DESC");
			rostIntNotes = rowI.getStringValue("RQST_INT_NOTES");
			rowI.clearAllRowsStatus();
	    	if(!approved.equals("Y")) 
	    	{
	    		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"You cannot create a voucher, this request hasn't been approved yet."});
	    		return;
	    	}
	    	if(reqId == null) 
	    	{
	    		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"You have to create/save Payment Request fist, before you can create a voucher."});
	    		return;
	    	}
	    	if(invcAmt == 0) 
	    	{
	    		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, Invoice Amount field is required."});
	    		return;
	    	}
	    	if(invcDate == null) 
	    	{
	    		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, Invoice Date field is required."});
	    		return;
	    	}
	    	if(vendId == null) 
	    	{
	    		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, Vendor field is required."});
	    		return;
	    	}
	    	rsReqLn = rsI.openChild("XT_FHI_PRF_NON_PO_LN");
	    	int childCount = 0;
	    	String body = "";
	    	
	    	if (rsReqLn != null) 
	        {
	        	rowReqLn = rsReqLn.getRowSet(); 
	         	RSIterator itReqLn = rsReqLn.findInit(RowSetInterface.ROW_Any,RowSetInterface.ROW_MarkDeleted,true);
	         	    
	         	while (itReqLn.next() != -60000) 
		        {
	         		acctId = rowReqLn.getStringValue("ACCT_ID");
	         		acctName = rowReqLn.getStringValue("XT_ACCT_NAME");
	             	orgId = rowReqLn.getStringValue("ORG_ID");
	             	orgName = rowReqLn.getStringValue("XT_ORG_NAME");
	             	projId = rowReqLn.getStringValue("PROJ_ID");
	             	projName = rowReqLn.getStringValue("XT_PROJ_NAME");
	             	rcvdAmt = rowReqLn.getDouble("XT_RCVD_AMT");
	             	totRcvdAmt = rowReqLn.getDouble("TRN_CST_AMT");


	             	if(acctId == null)
	            	{
	            		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, detail line's Account field is required."});
	            		return;
	            	}
	            	
	            	if(orgId == null) 
	            	{
	            		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, detail line's Organization field is required."});
	            		return;
	            	}
	            	
	            	if(rcvdAmt == null) 
	            	{
	            		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"To create a voucher, detail line's Received Amount field is required."});
	            		return;
	            	}
	            	
	         		if(childCount > 0) 
	     			{
	     				body = body + ",";
	     			
	     			}
					body = body + "{"
							+ "    \"row\":{"
							+ "        \"rsId\":\"APMVCHR_LN\","
							+ "        \"tranType\":\"INSERT\","
							+ "        \"data\":{"
							+ "            \"ACCT_ID\":\"" + acctId + "\","
							+ "            \"ACCT_NAME\":\"" + acctName + "\","
							+ "            \"AP_1099_FL\":\"" + ap1099FL + "\","
							+ "            \"CST_AMT_PCT_RT\":" + salesTaxRate + ","
							+ "            \"ORG_ID\":\"" + orgId + "\","
							+ "            \"ORG_NAME\":\"" + orgName + "\","
							+ "            \"PROJ_ID\":\"" + projId + "\","
							+ "            \"PROJ_NAME\":\"" + projName + "\","
							+ "            \"TRN_CST_AMT\":" + totRcvdAmt + ","
							+ "            \"TRN_SALES_TAX_AMT\":" + (transSaleTaxAmount * salesTaxRate) + ","
							+ "            \"APMVCHR_LN_NOTES\":\""
							+ (machantName + "|" + lnDescription + "|" + rostIntNotes).substring(0, Math.min((machantName + "|" + lnDescription + "|" + rostIntNotes).length(), 254)) + "\","
							+ "            \"TRN_TOT_BEF_DC_AMT\":" + (totRcvdAmt + (transSaleTaxAmount * salesTaxRate)) + ","
							+ "            \"VCHR_LN_DESC\":\""
							+ (machantName + "|" + lnDescription).substring(0, Math.min((machantName + "|" + lnDescription).length(), 30)) + "\","
							+ "            \"SALES_TAX_CD\":\""
							+ (transSaleTaxAmount != 0 ? "NCDURH" : null) + "\","
							+ "            \"TAXABLE_FL\":\""
							+ (transSaleTaxAmount != 0 ? "Y" : "N") + "\","
							+ "            \"RECOVERY_RT\":\""
							+ (transSaleTaxAmount != 0 ? "1.0000" : "0.0000") + "\"";

					if (ap1099FL != "N") {
						body = body + ", \"S_AP_1099_TYPE_CD\":\"" + typeCD1099 + "\"";
					}

					body = body + "         }"
							+ "     }"
							+ "}";
					childCount++;
	         		sumLineRcvdAmt = sumLineRcvdAmt + (rowReqLn.getDouble("XT_RCVD_AMT") == null ? 0 : rowReqLn.getDouble("XT_RCVD_AMT")); 
		        }	         	
	        }
	    	
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			       	   
			SqlManager sqlMgr = rsI.getSqlManager(this);
			
			String select = "SELECT FLE.CONT_FILE_NAME "
						  + "      ,FLE.CTYPE_ID "
						  + "      ,FLE.NOTES_TX "
						  + "  INTO :contentFileName "
						  + "	   ,:contentType "
						  + "      ,:contentNotes "
						  + "  FROM CMI_CONT_FILE_FLD FLD "
						  + "  LEFT OUTER JOIN CMI_CONT_FILE FLE "
						  + "    ON FLD.STORAGE_KEY = FLE.STORAGE_KEY "
						  + " WHERE FLD.CTYPE_FLD_ID = 'PAYMENT_REQUEST_ID' "
						  + "   AND FLD.FLD_VALUE = :reqId";
			
			sqlMgr.SqlExecuteQuery(select);
			String attachments = "";
			
			if(contentFileName != null && !contentFileName.trim().isEmpty())
			{
				attachments = attachments + ",{ "
										  + "     \"row\":{ "
										  + "       \"rsId\":\"CPM_CMI_LINKS\", "
										  + "       \"tranType\":\"INSERT\", "
										  + "       \"data\":{ "
										  + "         \"CONT_FILE_NAME\":\"" + contentFileName +"\", "
										  + "         \"CTYPE_ID\":\"" + contentType + "\", "
										  + "         \"NOTES_TX\":\"" + contentNotes + "\" "
										  + "     } "
										  + "   } "
										  + " }";
				
				while(sqlMgr.SqlFetchNext()) 
				{
					if(contentFileName != null && !contentFileName.trim().isEmpty())
					{
						attachments = attachments + ",{ "
								  + "     \"row\":{ "
								  + "       \"rsId\":\"CPM_CMI_LINKS\", "
								  + "       \"tranType\":\"INSERT\", "
								  + "       \"data\":{ "
								  + "         \"CONT_FILE_NAME\":\"" + contentFileName +"\", "
								  + "         \"CTYPE_ID\":\"" + contentType + "\", "
								  + "         \"NOTES_TX\":\"" + contentNotes + "\" "
								  + "     } "
								  + "   } "
								  + " }";
					}
				}
			}
			

					
//			////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	
	    	String header = "{"
						  + " \"document\":{"
						  + "  \"id\":\"xt_fhi_prf_apmvchr\","
						  + "  \"rows\":["
						  + "   {"
						  + "    \"row\":{"
						  + "     \"rsId\":\"APMVCHR_VCHRHDR\","
						  + "     \"tranType\":\"INSERT\","
						  + "     \"data\":{"
						  + "      \"FY_CD\":\"" + fy_cd + "\","
						  + "      \"INVC_DT\":\"" + String.valueOf(invcDate.getYear()+1900) + "-" + (invcDate.getMonth()+1 < 10 ? "0" + String.valueOf(invcDate.getMonth()+1) : String.valueOf(invcDate.getMonth()+1))  + "-" + (invcDate.getDate() < 10 ? "0" + String.valueOf(invcDate.getDate()) : String.valueOf(invcDate.getDate())) + "T00:00:00.0\","
						  + "      \"PD_NO\":" + pd_no + ","
						  + "      \"SUB_PD_NO\":" + sub_pd_no + ","
						  + "      \"TRN_INVC_AMT\":" + totInvcAmt + ","
						  + "      \"VEND_ID\":\"" + vendId + "\","
//	    				  + "      \"INCURRED_DT\":\"" + incurDate + "\"";
						  + "      \"XT_VCHR_HDR___INCURRED_DT\":\"" + String.valueOf(incurDate.getYear()+1900) + "-" + (incurDate.getMonth()+1 < 10 ? "0" + String.valueOf(incurDate.getMonth()+1) : String.valueOf(incurDate.getMonth()+1))  + "-" + (incurDate.getDate() < 10 ? "0" + String.valueOf(incurDate.getDate()) : String.valueOf(incurDate.getDate())) + "T00:00:00.0\"";
						  if(addrDC != null && !addrDC.equals(""))
						  {
							  header = header + ",      \"PAY_ADDR_DC\":\"" + addrDC + "\"";
						  }
						  if(cityName != null && !cityName.equals(""))
						  {
							  header = header + ",      \"ADR_CITY_NAME\":\"" + cityName + "\"";
						  }
						  if(country != null && !country.equals(""))
						  {
							  header = header + ",      \"ADR_COUNTRY_CD\":\"" + country + "\"";
						  }
						  if(line1addr != null && !line1addr.equals(""))
						  {
							  header = header + ",      \"ADR_LN_1_ADDR\":\"" + line1addr + "\"";
						  }
						  if(line2addr != null && !line2addr.equals(""))
						  {
							  header = header + ",      \"ADR_LN_2_ADDR\":\"" + line2addr + "\"";
						  }
						  if(line3addr != null && !line3addr.equals(""))
						  {
							  header = header + ",      \"ADR_LN_3_ADDR\":\"" + line3addr + "\"";
						  }
						  if(state != null && !state.equals(""))
						  {
							  header = header + ",      \"ADR_MAIL_STATE_DC\":\"" + state + "\"";
						  }
						  if(vendName != null && !vendName.equals(""))
						  {
							  header = header + ",      \"ADR_PAY_VEND_NAME\":\"" + vendName + "\"";
						  }
						  if(postalCode != null && !postalCode.equals(""))
						  {
							  header = header + ",      \"ADR_POSTAL_CD\":\"" + postalCode + "\"";
						  }
						  if(currency != null && !currency.equals(""))
						  {
							  header = header + ",      \"TRN_CRNCY_CD\":\"" + currency + "\"";
						  }
						  if(shipId != null && !shipId.equals(""))
						  {
							  header = header + ",      \"TRN_LOC_CD\":\"" + shipId + "\"";
						  }
						  if(ap1099TaxId != null && !ap1099TaxId.equals(""))
						  {
							  header = header + ",      \"VAT_TAX_ID\":\"" + ap1099TaxId + "\"";
						  }
						  if(invcId != null && !invcId.equals(""))
						  {
							  header = header + ",      \"INVC_ID\":\"" + invcId + "\"";
						  }
			header +=	    "      },"
						  + "     \"children\":[";
	    	
	    	String footer = "     ]"
						  + "    }"
						  + "   }"
						  + "  ]"
						  + " }"
						  + "}";
	    	
	    	String request = header + body + attachments + footer;
	    	
	    	try 
			{
				InvokeLocalGenericWS wsProc = appI.getInvokeLocalGenericWS();
                MethodResponseInterface mriResponse;

                mriResponse = wsProc.cpwwsgenericimport(request, companyId);
                Message messages[] = mriResponse.getMessage();

                for(Message m:messages)
                {
                	if(m.getMsgSeverity() < 3)
                	{
                		vchr = tryParseInt(m.getMsgText().substring(0, m.getMsgText().indexOf(' ')), 0);
                		
            			if(vchr != 0)
        			    {
        			    	rowI.setint(vchr, "XT_VCHR_NO");
        			    	ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.WARNING, new String[] {vchr + " has been assigned to new AP Voucher."});
        			    }  
                	}
                	else 
                	{
                		ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"Error Sevirity:" + m.getMsgSeverity() + " -- " + m.getMsgText()});
                	}               		
                }

				saveVoucherToDB(rsI);
			}
			catch(Exception ex)
			{
				ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"ERROR: " + ex.getMessage()});
			}
		}	
		else
		{
			ai.addMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"AP voucher " + vchr + " has been already created for this Non-PO Paymen Request."});
		}
	}
	
	private boolean saveVoucherToDB(ResultSetInterface rsI) throws SQLException, DEException
	{
		boolean result = false;
		
		SqlManager sqlMgr = rsI.getSqlManager(this);
		
		String update = new StringBuffer()
				.append("UPDATE XT_FHI_PRF_NON_PO_HDR ")
				.append("SET VCHR_NO = :vchr ")
				.append("WHERE REQUEST_ID = :reqId").toString();
		try
		{
			sqlMgr.SqlPrepareAndExecute(update, true);
			result = true;
		}
		catch(Exception ex)
		{
			rsI.addRSMessage("XT_FHI_PRF_NON_PO__MSG", MessageTypes.ERROR, new String[] {"Unable to save Voucher Number with the request. ERROR: " + ex.getMessage()});
		}

		return result;
	}
	private int tryParseInt(String value, int defaultVal) 
	{
	    try 
	    {
	        return Integer.parseInt(value);
	    } 
	    catch (NumberFormatException e) 
	    {
	        return defaultVal;
	    }
	}
	
}
