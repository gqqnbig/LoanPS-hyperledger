package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		submitloanrequestmodule_service = ServiceManager.createSubmitLoanRequestModule();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		entervalidatedcreditreferencesmodule_service = ServiceManager.createEnterValidatedCreditReferencesModule();
		evaluateloanrequestmodule_service = ServiceManager.createEvaluateLoanRequestModule();
		generateloanletterandagreementmodule_service = ServiceManager.createGenerateLoanLetterAndAgreementModule();
		loanprocessingsystemsystem_service = ServiceManager.createLoanProcessingSystemSystem();
		manageloantermcrudservice_service = ServiceManager.createManageLoanTermCRUDService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = SubmitLoanRequestModuleImpl.opINVRelatedEntity.get("enterLoanInformation");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("enterLoanInformation" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SubmitLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("enterLoanInformation", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SubmitLoanRequestModuleImpl.opINVRelatedEntity.get("creditRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("creditRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SubmitLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("creditRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SubmitLoanRequestModuleImpl.opINVRelatedEntity.get("accountStatusRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("accountStatusRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SubmitLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("accountStatusRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = SubmitLoanRequestModuleImpl.opINVRelatedEntity.get("calculateScore");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("calculateScore" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("SubmitLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("calculateScore", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EnterValidatedCreditReferencesModuleImpl.opINVRelatedEntity.get("listSubmitedLoanRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listSubmitedLoanRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EnterValidatedCreditReferencesModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listSubmitedLoanRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EnterValidatedCreditReferencesModuleImpl.opINVRelatedEntity.get("chooseLoanRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseLoanRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EnterValidatedCreditReferencesModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseLoanRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EnterValidatedCreditReferencesModuleImpl.opINVRelatedEntity.get("markRequestValid");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("markRequestValid" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EnterValidatedCreditReferencesModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("markRequestValid", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("listTenLoanRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listTenLoanRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listTenLoanRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("chooseOneForReview");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("chooseOneForReview" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("chooseOneForReview", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("checkCreditHistory");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("checkCreditHistory" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("checkCreditHistory", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("reviewCheckingAccount");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("reviewCheckingAccount" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("reviewCheckingAccount", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("listAvaiableLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listAvaiableLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listAvaiableLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("addLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("addLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("addLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = EvaluateLoanRequestModuleImpl.opINVRelatedEntity.get("approveLoanRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("approveLoanRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("EvaluateLoanRequestModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("approveLoanRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = GenerateLoanLetterAndAgreementModuleImpl.opINVRelatedEntity.get("listApprovalRequest");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listApprovalRequest" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("GenerateLoanLetterAndAgreementModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listApprovalRequest", v);
		
		v = new VBox();
		
		//entities invariants
		entities = GenerateLoanLetterAndAgreementModuleImpl.opINVRelatedEntity.get("genereateApprovalLetter");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("genereateApprovalLetter" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("GenerateLoanLetterAndAgreementModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("genereateApprovalLetter", v);
		
		v = new VBox();
		
		//entities invariants
		entities = GenerateLoanLetterAndAgreementModuleImpl.opINVRelatedEntity.get("emailToAppliant");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("emailToAppliant" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("GenerateLoanLetterAndAgreementModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("emailToAppliant", v);
		
		v = new VBox();
		
		//entities invariants
		entities = GenerateLoanLetterAndAgreementModuleImpl.opINVRelatedEntity.get("generateLoanAgreement");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("generateLoanAgreement" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("GenerateLoanLetterAndAgreementModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("generateLoanAgreement", v);
		
		v = new VBox();
		
		//entities invariants
		entities = GenerateLoanLetterAndAgreementModuleImpl.opINVRelatedEntity.get("printLoanAgreement");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("printLoanAgreement" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("GenerateLoanLetterAndAgreementModule")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("printLoanAgreement", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("bookNewLoan");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("bookNewLoan" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("bookNewLoan", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("generateStandardPaymentNotice");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("generateStandardPaymentNotice" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("generateStandardPaymentNotice", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("generateLateNotice");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("generateLateNotice" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("generateLateNotice", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("loanPayment");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("loanPayment" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("loanPayment", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("closeOutLoan");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("closeOutLoan" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("closeOutLoan", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageLoanTermCRUDServiceImpl.opINVRelatedEntity.get("createLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageLoanTermCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageLoanTermCRUDServiceImpl.opINVRelatedEntity.get("queryLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("queryLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageLoanTermCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("queryLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageLoanTermCRUDServiceImpl.opINVRelatedEntity.get("modifyLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("modifyLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageLoanTermCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("modifyLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ManageLoanTermCRUDServiceImpl.opINVRelatedEntity.get("deleteLoanTerm");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("deleteLoanTerm" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ManageLoanTermCRUDService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("deleteLoanTerm", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("getCheckingAccountStatus");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getCheckingAccountStatus" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getCheckingAccountStatus", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("getCreditHistory");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("getCreditHistory" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("getCreditHistory", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("sendEmail");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("sendEmail" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("sendEmail", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("print");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("print" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("print", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("createLoanAccount");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("createLoanAccount" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("createLoanAccount", v);
		
		v = new VBox();
		
		//entities invariants
		entities = ThirdPartyServicesImpl.opINVRelatedEntity.get("transferFunds");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("transferFunds" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("ThirdPartyServices")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("transferFunds", v);
		
		v = new VBox();
		
		//entities invariants
		entities = LoanProcessingSystemSystemImpl.opINVRelatedEntity.get("listBookedLoans");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("listBookedLoans" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("LoanProcessingSystemSystem")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("listBookedLoans", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
		tableLoanRequest_Status.setMinWidth("Status".length()*10);
		tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_Status);
		TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
		tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
		tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
		TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
		tableLoanRequest_Name.setMinWidth("Name".length()*10);
		tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_Name);
		TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
		tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
		tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
		TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
		tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
		tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
		TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
		tableLoanRequest_Income.setMinWidth("Income".length()*10);
		tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_Income);
		TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
		tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
		tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
		TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
		tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
		tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
		TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
		tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
		tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
		TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
		tableLoanRequest_Email.setMinWidth("Email".length()*10);
		tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_Email);
		TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
		tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
		tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
		TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
		tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
		tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
		TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
		tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
		tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
		TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
		tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
		tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
		TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
		tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
		tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
		    }
		});	
		tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
		
		//table data
		ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
		List<LoanRequest> rsLoanRequest = EntityManager.getAllInstancesOf("LoanRequest");
		for (LoanRequest r : rsLoanRequest) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Status", String.valueOf(r.getStatus()));
			unit.put("RequestID", String.valueOf(r.getRequestID()));
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
			if (r.getLoanPurpose() != null)
				unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
			else
				unit.put("LoanPurpose", "");
			unit.put("Income", String.valueOf(r.getIncome()));
			unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
			if (r.getPostalAddress() != null)
				unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
			else
				unit.put("PostalAddress", "");
			unit.put("ZipCode", String.valueOf(r.getZipCode()));
			if (r.getEmail() != null)
				unit.put("Email", String.valueOf(r.getEmail()));
			else
				unit.put("Email", "");
			if (r.getWorkReferences() != null)
				unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
			else
				unit.put("WorkReferences", "");
			if (r.getCreditReferences() != null)
				unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
			else
				unit.put("CreditReferences", "");
			unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
			unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
			unit.put("CreditScore", String.valueOf(r.getCreditScore()));

			dataLoanRequest.add(unit);
		}
		
		tableLoanRequest.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLoanRequest.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("LoanRequest", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLoanRequest.setItems(dataLoanRequest);
		allObjectTables.put("LoanRequest", tableLoanRequest);
		
		TableView<Map<String, String>> tableLoan = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLoan_LoanID = new TableColumn<Map<String, String>, String>("LoanID");
		tableLoan_LoanID.setMinWidth("LoanID".length()*10);
		tableLoan_LoanID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LoanID"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_LoanID);
		TableColumn<Map<String, String>, String> tableLoan_RemainAmountToPay = new TableColumn<Map<String, String>, String>("RemainAmountToPay");
		tableLoan_RemainAmountToPay.setMinWidth("RemainAmountToPay".length()*10);
		tableLoan_RemainAmountToPay.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RemainAmountToPay"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_RemainAmountToPay);
		TableColumn<Map<String, String>, String> tableLoan_Status = new TableColumn<Map<String, String>, String>("Status");
		tableLoan_Status.setMinWidth("Status".length()*10);
		tableLoan_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_Status);
		TableColumn<Map<String, String>, String> tableLoan_IsPaidinFull = new TableColumn<Map<String, String>, String>("IsPaidinFull");
		tableLoan_IsPaidinFull.setMinWidth("IsPaidinFull".length()*10);
		tableLoan_IsPaidinFull.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("IsPaidinFull"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_IsPaidinFull);
		TableColumn<Map<String, String>, String> tableLoan_StartDate = new TableColumn<Map<String, String>, String>("StartDate");
		tableLoan_StartDate.setMinWidth("StartDate".length()*10);
		tableLoan_StartDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("StartDate"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_StartDate);
		TableColumn<Map<String, String>, String> tableLoan_EndDate = new TableColumn<Map<String, String>, String>("EndDate");
		tableLoan_EndDate.setMinWidth("EndDate".length()*10);
		tableLoan_EndDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("EndDate"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_EndDate);
		TableColumn<Map<String, String>, String> tableLoan_CurrentOverDueDate = new TableColumn<Map<String, String>, String>("CurrentOverDueDate");
		tableLoan_CurrentOverDueDate.setMinWidth("CurrentOverDueDate".length()*10);
		tableLoan_CurrentOverDueDate.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("CurrentOverDueDate"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_CurrentOverDueDate);
		TableColumn<Map<String, String>, String> tableLoan_RePaymentDays = new TableColumn<Map<String, String>, String>("RePaymentDays");
		tableLoan_RePaymentDays.setMinWidth("RePaymentDays".length()*10);
		tableLoan_RePaymentDays.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RePaymentDays"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_RePaymentDays);
		TableColumn<Map<String, String>, String> tableLoan_RepaymentAmount = new TableColumn<Map<String, String>, String>("RepaymentAmount");
		tableLoan_RepaymentAmount.setMinWidth("RepaymentAmount".length()*10);
		tableLoan_RepaymentAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("RepaymentAmount"));
		    }
		});	
		tableLoan.getColumns().add(tableLoan_RepaymentAmount);
		
		//table data
		ObservableList<Map<String, String>> dataLoan = FXCollections.observableArrayList();
		List<Loan> rsLoan = EntityManager.getAllInstancesOf("Loan");
		for (Loan r : rsLoan) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("LoanID", String.valueOf(r.getLoanID()));
			unit.put("RemainAmountToPay", String.valueOf(r.getRemainAmountToPay()));
			unit.put("Status", String.valueOf(r.getStatus()));
			unit.put("IsPaidinFull", String.valueOf(r.getIsPaidinFull()));
			if (r.getStartDate() != null)
				unit.put("StartDate", r.getStartDate().format(dateformatter));
			else
				unit.put("StartDate", "");
			if (r.getEndDate() != null)
				unit.put("EndDate", r.getEndDate().format(dateformatter));
			else
				unit.put("EndDate", "");
			if (r.getCurrentOverDueDate() != null)
				unit.put("CurrentOverDueDate", r.getCurrentOverDueDate().format(dateformatter));
			else
				unit.put("CurrentOverDueDate", "");
			unit.put("RePaymentDays", String.valueOf(r.getRePaymentDays()));
			unit.put("RepaymentAmount", String.valueOf(r.getRepaymentAmount()));

			dataLoan.add(unit);
		}
		
		tableLoan.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLoan.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Loan", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLoan.setItems(dataLoan);
		allObjectTables.put("Loan", tableLoan);
		
		TableView<Map<String, String>> tableLoanTerm = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLoanTerm_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
		tableLoanTerm_ItemID.setMinWidth("ItemID".length()*10);
		tableLoanTerm_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
		    }
		});	
		tableLoanTerm.getColumns().add(tableLoanTerm_ItemID);
		TableColumn<Map<String, String>, String> tableLoanTerm_Content = new TableColumn<Map<String, String>, String>("Content");
		tableLoanTerm_Content.setMinWidth("Content".length()*10);
		tableLoanTerm_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
		    }
		});	
		tableLoanTerm.getColumns().add(tableLoanTerm_Content);
		
		//table data
		ObservableList<Map<String, String>> dataLoanTerm = FXCollections.observableArrayList();
		List<LoanTerm> rsLoanTerm = EntityManager.getAllInstancesOf("LoanTerm");
		for (LoanTerm r : rsLoanTerm) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("ItemID", String.valueOf(r.getItemID()));
			if (r.getContent() != null)
				unit.put("Content", String.valueOf(r.getContent()));
			else
				unit.put("Content", "");

			dataLoanTerm.add(unit);
		}
		
		tableLoanTerm.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLoanTerm.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("LoanTerm", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLoanTerm.setItems(dataLoanTerm);
		allObjectTables.put("LoanTerm", tableLoanTerm);
		
		TableView<Map<String, String>> tableCheckingAccount = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCheckingAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
		tableCheckingAccount_Balance.setMinWidth("Balance".length()*10);
		tableCheckingAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
		    }
		});	
		tableCheckingAccount.getColumns().add(tableCheckingAccount_Balance);
		TableColumn<Map<String, String>, String> tableCheckingAccount_Status = new TableColumn<Map<String, String>, String>("Status");
		tableCheckingAccount_Status.setMinWidth("Status".length()*10);
		tableCheckingAccount_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableCheckingAccount.getColumns().add(tableCheckingAccount_Status);
		
		//table data
		ObservableList<Map<String, String>> dataCheckingAccount = FXCollections.observableArrayList();
		List<CheckingAccount> rsCheckingAccount = EntityManager.getAllInstancesOf("CheckingAccount");
		for (CheckingAccount r : rsCheckingAccount) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("Balance", String.valueOf(r.getBalance()));
			unit.put("Status", String.valueOf(r.getStatus()));

			dataCheckingAccount.add(unit);
		}
		
		tableCheckingAccount.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCheckingAccount.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CheckingAccount", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCheckingAccount.setItems(dataCheckingAccount);
		allObjectTables.put("CheckingAccount", tableCheckingAccount);
		
		TableView<Map<String, String>> tableCreditHistory = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableCreditHistory_OutstandingDebt = new TableColumn<Map<String, String>, String>("OutstandingDebt");
		tableCreditHistory_OutstandingDebt.setMinWidth("OutstandingDebt".length()*10);
		tableCreditHistory_OutstandingDebt.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("OutstandingDebt"));
		    }
		});	
		tableCreditHistory.getColumns().add(tableCreditHistory_OutstandingDebt);
		TableColumn<Map<String, String>, String> tableCreditHistory_BadDebits = new TableColumn<Map<String, String>, String>("BadDebits");
		tableCreditHistory_BadDebits.setMinWidth("BadDebits".length()*10);
		tableCreditHistory_BadDebits.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("BadDebits"));
		    }
		});	
		tableCreditHistory.getColumns().add(tableCreditHistory_BadDebits);
		
		//table data
		ObservableList<Map<String, String>> dataCreditHistory = FXCollections.observableArrayList();
		List<CreditHistory> rsCreditHistory = EntityManager.getAllInstancesOf("CreditHistory");
		for (CreditHistory r : rsCreditHistory) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("OutstandingDebt", String.valueOf(r.getOutstandingDebt()));
			unit.put("BadDebits", String.valueOf(r.getBadDebits()));

			dataCreditHistory.add(unit);
		}
		
		tableCreditHistory.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableCreditHistory.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("CreditHistory", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableCreditHistory.setItems(dataCreditHistory);
		allObjectTables.put("CreditHistory", tableCreditHistory);
		
		TableView<Map<String, String>> tableLoanAccount = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLoanAccount_LoanAccountID = new TableColumn<Map<String, String>, String>("LoanAccountID");
		tableLoanAccount_LoanAccountID.setMinWidth("LoanAccountID".length()*10);
		tableLoanAccount_LoanAccountID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("LoanAccountID"));
		    }
		});	
		tableLoanAccount.getColumns().add(tableLoanAccount_LoanAccountID);
		TableColumn<Map<String, String>, String> tableLoanAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
		tableLoanAccount_Balance.setMinWidth("Balance".length()*10);
		tableLoanAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
		    }
		});	
		tableLoanAccount.getColumns().add(tableLoanAccount_Balance);
		TableColumn<Map<String, String>, String> tableLoanAccount_Status = new TableColumn<Map<String, String>, String>("Status");
		tableLoanAccount_Status.setMinWidth("Status".length()*10);
		tableLoanAccount_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
		    }
		});	
		tableLoanAccount.getColumns().add(tableLoanAccount_Status);
		
		//table data
		ObservableList<Map<String, String>> dataLoanAccount = FXCollections.observableArrayList();
		List<LoanAccount> rsLoanAccount = EntityManager.getAllInstancesOf("LoanAccount");
		for (LoanAccount r : rsLoanAccount) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			unit.put("LoanAccountID", String.valueOf(r.getLoanAccountID()));
			unit.put("Balance", String.valueOf(r.getBalance()));
			unit.put("Status", String.valueOf(r.getStatus()));

			dataLoanAccount.add(unit);
		}
		
		tableLoanAccount.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLoanAccount.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("LoanAccount", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLoanAccount.setItems(dataLoanAccount);
		allObjectTables.put("LoanAccount", tableLoanAccount);
		
		TableView<Map<String, String>> tableApprovalLetter = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableApprovalLetter_Content = new TableColumn<Map<String, String>, String>("Content");
		tableApprovalLetter_Content.setMinWidth("Content".length()*10);
		tableApprovalLetter_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
		    }
		});	
		tableApprovalLetter.getColumns().add(tableApprovalLetter_Content);
		
		//table data
		ObservableList<Map<String, String>> dataApprovalLetter = FXCollections.observableArrayList();
		List<ApprovalLetter> rsApprovalLetter = EntityManager.getAllInstancesOf("ApprovalLetter");
		for (ApprovalLetter r : rsApprovalLetter) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getContent() != null)
				unit.put("Content", String.valueOf(r.getContent()));
			else
				unit.put("Content", "");

			dataApprovalLetter.add(unit);
		}
		
		tableApprovalLetter.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableApprovalLetter.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("ApprovalLetter", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableApprovalLetter.setItems(dataApprovalLetter);
		allObjectTables.put("ApprovalLetter", tableApprovalLetter);
		
		TableView<Map<String, String>> tableLoanAgreement = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableLoanAgreement_Content = new TableColumn<Map<String, String>, String>("Content");
		tableLoanAgreement_Content.setMinWidth("Content".length()*10);
		tableLoanAgreement_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
		    }
		});	
		tableLoanAgreement.getColumns().add(tableLoanAgreement_Content);
		
		//table data
		ObservableList<Map<String, String>> dataLoanAgreement = FXCollections.observableArrayList();
		List<LoanAgreement> rsLoanAgreement = EntityManager.getAllInstancesOf("LoanAgreement");
		for (LoanAgreement r : rsLoanAgreement) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getContent() != null)
				unit.put("Content", String.valueOf(r.getContent()));
			else
				unit.put("Content", "");

			dataLoanAgreement.add(unit);
		}
		
		tableLoanAgreement.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableLoanAgreement.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("LoanAgreement", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableLoanAgreement.setItems(dataLoanAgreement);
		allObjectTables.put("LoanAgreement", tableLoanAgreement);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateLoanRequestTable(List<LoanRequest> rsLoanRequest) {
			ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
			for (LoanRequest r : rsLoanRequest) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Status", String.valueOf(r.getStatus()));
				unit.put("RequestID", String.valueOf(r.getRequestID()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
				if (r.getLoanPurpose() != null)
					unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
				else
					unit.put("LoanPurpose", "");
				unit.put("Income", String.valueOf(r.getIncome()));
				unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
				if (r.getPostalAddress() != null)
					unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
				else
					unit.put("PostalAddress", "");
				unit.put("ZipCode", String.valueOf(r.getZipCode()));
				if (r.getEmail() != null)
					unit.put("Email", String.valueOf(r.getEmail()));
				else
					unit.put("Email", "");
				if (r.getWorkReferences() != null)
					unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
				else
					unit.put("WorkReferences", "");
				if (r.getCreditReferences() != null)
					unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
				else
					unit.put("CreditReferences", "");
				unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
				unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
				unit.put("CreditScore", String.valueOf(r.getCreditScore()));
				dataLoanRequest.add(unit);
			}
			
			allObjectTables.get("LoanRequest").setItems(dataLoanRequest);
	}
	public void updateLoanTable(List<Loan> rsLoan) {
			ObservableList<Map<String, String>> dataLoan = FXCollections.observableArrayList();
			for (Loan r : rsLoan) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("LoanID", String.valueOf(r.getLoanID()));
				unit.put("RemainAmountToPay", String.valueOf(r.getRemainAmountToPay()));
				unit.put("Status", String.valueOf(r.getStatus()));
				unit.put("IsPaidinFull", String.valueOf(r.getIsPaidinFull()));
				if (r.getStartDate() != null)
					unit.put("StartDate", r.getStartDate().format(dateformatter));
				else
					unit.put("StartDate", "");
				if (r.getEndDate() != null)
					unit.put("EndDate", r.getEndDate().format(dateformatter));
				else
					unit.put("EndDate", "");
				if (r.getCurrentOverDueDate() != null)
					unit.put("CurrentOverDueDate", r.getCurrentOverDueDate().format(dateformatter));
				else
					unit.put("CurrentOverDueDate", "");
				unit.put("RePaymentDays", String.valueOf(r.getRePaymentDays()));
				unit.put("RepaymentAmount", String.valueOf(r.getRepaymentAmount()));
				dataLoan.add(unit);
			}
			
			allObjectTables.get("Loan").setItems(dataLoan);
	}
	public void updateLoanTermTable(List<LoanTerm> rsLoanTerm) {
			ObservableList<Map<String, String>> dataLoanTerm = FXCollections.observableArrayList();
			for (LoanTerm r : rsLoanTerm) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("ItemID", String.valueOf(r.getItemID()));
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataLoanTerm.add(unit);
			}
			
			allObjectTables.get("LoanTerm").setItems(dataLoanTerm);
	}
	public void updateCheckingAccountTable(List<CheckingAccount> rsCheckingAccount) {
			ObservableList<Map<String, String>> dataCheckingAccount = FXCollections.observableArrayList();
			for (CheckingAccount r : rsCheckingAccount) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("Status", String.valueOf(r.getStatus()));
				dataCheckingAccount.add(unit);
			}
			
			allObjectTables.get("CheckingAccount").setItems(dataCheckingAccount);
	}
	public void updateCreditHistoryTable(List<CreditHistory> rsCreditHistory) {
			ObservableList<Map<String, String>> dataCreditHistory = FXCollections.observableArrayList();
			for (CreditHistory r : rsCreditHistory) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("OutstandingDebt", String.valueOf(r.getOutstandingDebt()));
				unit.put("BadDebits", String.valueOf(r.getBadDebits()));
				dataCreditHistory.add(unit);
			}
			
			allObjectTables.get("CreditHistory").setItems(dataCreditHistory);
	}
	public void updateLoanAccountTable(List<LoanAccount> rsLoanAccount) {
			ObservableList<Map<String, String>> dataLoanAccount = FXCollections.observableArrayList();
			for (LoanAccount r : rsLoanAccount) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				unit.put("LoanAccountID", String.valueOf(r.getLoanAccountID()));
				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("Status", String.valueOf(r.getStatus()));
				dataLoanAccount.add(unit);
			}
			
			allObjectTables.get("LoanAccount").setItems(dataLoanAccount);
	}
	public void updateApprovalLetterTable(List<ApprovalLetter> rsApprovalLetter) {
			ObservableList<Map<String, String>> dataApprovalLetter = FXCollections.observableArrayList();
			for (ApprovalLetter r : rsApprovalLetter) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataApprovalLetter.add(unit);
			}
			
			allObjectTables.get("ApprovalLetter").setItems(dataApprovalLetter);
	}
	public void updateLoanAgreementTable(List<LoanAgreement> rsLoanAgreement) {
			ObservableList<Map<String, String>> dataLoanAgreement = FXCollections.observableArrayList();
			for (LoanAgreement r : rsLoanAgreement) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataLoanAgreement.add(unit);
			}
			
			allObjectTables.get("LoanAgreement").setItems(dataLoanAgreement);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateLoanRequestTable() {
			ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
			List<LoanRequest> rsLoanRequest = EntityManager.getAllInstancesOf("LoanRequest");
			for (LoanRequest r : rsLoanRequest) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Status", String.valueOf(r.getStatus()));
				unit.put("RequestID", String.valueOf(r.getRequestID()));
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
				if (r.getLoanPurpose() != null)
					unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
				else
					unit.put("LoanPurpose", "");
				unit.put("Income", String.valueOf(r.getIncome()));
				unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
				if (r.getPostalAddress() != null)
					unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
				else
					unit.put("PostalAddress", "");
				unit.put("ZipCode", String.valueOf(r.getZipCode()));
				if (r.getEmail() != null)
					unit.put("Email", String.valueOf(r.getEmail()));
				else
					unit.put("Email", "");
				if (r.getWorkReferences() != null)
					unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
				else
					unit.put("WorkReferences", "");
				if (r.getCreditReferences() != null)
					unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
				else
					unit.put("CreditReferences", "");
				unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
				unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
				unit.put("CreditScore", String.valueOf(r.getCreditScore()));
				dataLoanRequest.add(unit);
			}
			
			allObjectTables.get("LoanRequest").setItems(dataLoanRequest);
	}
	public void updateLoanTable() {
			ObservableList<Map<String, String>> dataLoan = FXCollections.observableArrayList();
			List<Loan> rsLoan = EntityManager.getAllInstancesOf("Loan");
			for (Loan r : rsLoan) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("LoanID", String.valueOf(r.getLoanID()));
				unit.put("RemainAmountToPay", String.valueOf(r.getRemainAmountToPay()));
				unit.put("Status", String.valueOf(r.getStatus()));
				unit.put("IsPaidinFull", String.valueOf(r.getIsPaidinFull()));
				if (r.getStartDate() != null)
					unit.put("StartDate", r.getStartDate().format(dateformatter));
				else
					unit.put("StartDate", "");
				if (r.getEndDate() != null)
					unit.put("EndDate", r.getEndDate().format(dateformatter));
				else
					unit.put("EndDate", "");
				if (r.getCurrentOverDueDate() != null)
					unit.put("CurrentOverDueDate", r.getCurrentOverDueDate().format(dateformatter));
				else
					unit.put("CurrentOverDueDate", "");
				unit.put("RePaymentDays", String.valueOf(r.getRePaymentDays()));
				unit.put("RepaymentAmount", String.valueOf(r.getRepaymentAmount()));
				dataLoan.add(unit);
			}
			
			allObjectTables.get("Loan").setItems(dataLoan);
	}
	public void updateLoanTermTable() {
			ObservableList<Map<String, String>> dataLoanTerm = FXCollections.observableArrayList();
			List<LoanTerm> rsLoanTerm = EntityManager.getAllInstancesOf("LoanTerm");
			for (LoanTerm r : rsLoanTerm) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("ItemID", String.valueOf(r.getItemID()));
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataLoanTerm.add(unit);
			}
			
			allObjectTables.get("LoanTerm").setItems(dataLoanTerm);
	}
	public void updateCheckingAccountTable() {
			ObservableList<Map<String, String>> dataCheckingAccount = FXCollections.observableArrayList();
			List<CheckingAccount> rsCheckingAccount = EntityManager.getAllInstancesOf("CheckingAccount");
			for (CheckingAccount r : rsCheckingAccount) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("Status", String.valueOf(r.getStatus()));
				dataCheckingAccount.add(unit);
			}
			
			allObjectTables.get("CheckingAccount").setItems(dataCheckingAccount);
	}
	public void updateCreditHistoryTable() {
			ObservableList<Map<String, String>> dataCreditHistory = FXCollections.observableArrayList();
			List<CreditHistory> rsCreditHistory = EntityManager.getAllInstancesOf("CreditHistory");
			for (CreditHistory r : rsCreditHistory) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("OutstandingDebt", String.valueOf(r.getOutstandingDebt()));
				unit.put("BadDebits", String.valueOf(r.getBadDebits()));
				dataCreditHistory.add(unit);
			}
			
			allObjectTables.get("CreditHistory").setItems(dataCreditHistory);
	}
	public void updateLoanAccountTable() {
			ObservableList<Map<String, String>> dataLoanAccount = FXCollections.observableArrayList();
			List<LoanAccount> rsLoanAccount = EntityManager.getAllInstancesOf("LoanAccount");
			for (LoanAccount r : rsLoanAccount) {
				Map<String, String> unit = new HashMap<String, String>();


				unit.put("LoanAccountID", String.valueOf(r.getLoanAccountID()));
				unit.put("Balance", String.valueOf(r.getBalance()));
				unit.put("Status", String.valueOf(r.getStatus()));
				dataLoanAccount.add(unit);
			}
			
			allObjectTables.get("LoanAccount").setItems(dataLoanAccount);
	}
	public void updateApprovalLetterTable() {
			ObservableList<Map<String, String>> dataApprovalLetter = FXCollections.observableArrayList();
			List<ApprovalLetter> rsApprovalLetter = EntityManager.getAllInstancesOf("ApprovalLetter");
			for (ApprovalLetter r : rsApprovalLetter) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataApprovalLetter.add(unit);
			}
			
			allObjectTables.get("ApprovalLetter").setItems(dataApprovalLetter);
	}
	public void updateLoanAgreementTable() {
			ObservableList<Map<String, String>> dataLoanAgreement = FXCollections.observableArrayList();
			List<LoanAgreement> rsLoanAgreement = EntityManager.getAllInstancesOf("LoanAgreement");
			for (LoanAgreement r : rsLoanAgreement) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				dataLoanAgreement.add(unit);
			}
			
			allObjectTables.get("LoanAgreement").setItems(dataLoanAgreement);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 loanrequest = new ClassInfo("LoanRequest", EntityManager.getAllInstancesOf("LoanRequest").size());
	 	 classInfodata.add(loanrequest);
	 	 loan = new ClassInfo("Loan", EntityManager.getAllInstancesOf("Loan").size());
	 	 classInfodata.add(loan);
	 	 loanterm = new ClassInfo("LoanTerm", EntityManager.getAllInstancesOf("LoanTerm").size());
	 	 classInfodata.add(loanterm);
	 	 checkingaccount = new ClassInfo("CheckingAccount", EntityManager.getAllInstancesOf("CheckingAccount").size());
	 	 classInfodata.add(checkingaccount);
	 	 credithistory = new ClassInfo("CreditHistory", EntityManager.getAllInstancesOf("CreditHistory").size());
	 	 classInfodata.add(credithistory);
	 	 loanaccount = new ClassInfo("LoanAccount", EntityManager.getAllInstancesOf("LoanAccount").size());
	 	 classInfodata.add(loanaccount);
	 	 approvalletter = new ClassInfo("ApprovalLetter", EntityManager.getAllInstancesOf("ApprovalLetter").size());
	 	 classInfodata.add(approvalletter);
	 	 loanagreement = new ClassInfo("LoanAgreement", EntityManager.getAllInstancesOf("LoanAgreement").size());
	 	 classInfodata.add(loanagreement);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 loanrequest.setNumber(EntityManager.getAllInstancesOf("LoanRequest").size());
	 	 loan.setNumber(EntityManager.getAllInstancesOf("Loan").size());
	 	 loanterm.setNumber(EntityManager.getAllInstancesOf("LoanTerm").size());
	 	 checkingaccount.setNumber(EntityManager.getAllInstancesOf("CheckingAccount").size());
	 	 credithistory.setNumber(EntityManager.getAllInstancesOf("CreditHistory").size());
	 	 loanaccount.setNumber(EntityManager.getAllInstancesOf("LoanAccount").size());
	 	 approvalletter.setNumber(EntityManager.getAllInstancesOf("ApprovalLetter").size());
	 	 loanagreement.setNumber(EntityManager.getAllInstancesOf("LoanAgreement").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> LoanRequest_association_data = FXCollections.observableArrayList();
		AssociationInfo LoanRequest_associatition_ApprovalLoan = new AssociationInfo("LoanRequest", "Loan", "ApprovalLoan", false);
		LoanRequest_association_data.add(LoanRequest_associatition_ApprovalLoan);
		AssociationInfo LoanRequest_associatition_RequestedCAHistory = new AssociationInfo("LoanRequest", "CheckingAccount", "RequestedCAHistory", false);
		LoanRequest_association_data.add(LoanRequest_associatition_RequestedCAHistory);
		AssociationInfo LoanRequest_associatition_RequestedCreditHistory = new AssociationInfo("LoanRequest", "CreditHistory", "RequestedCreditHistory", false);
		LoanRequest_association_data.add(LoanRequest_associatition_RequestedCreditHistory);
		AssociationInfo LoanRequest_associatition_AttachedApprovalLetter = new AssociationInfo("LoanRequest", "ApprovalLetter", "AttachedApprovalLetter", false);
		LoanRequest_association_data.add(LoanRequest_associatition_AttachedApprovalLetter);
		AssociationInfo LoanRequest_associatition_AttachedLoanAgreement = new AssociationInfo("LoanRequest", "LoanAgreement", "AttachedLoanAgreement", false);
		LoanRequest_association_data.add(LoanRequest_associatition_AttachedLoanAgreement);
		AssociationInfo LoanRequest_associatition_AttachedLoanTerms = new AssociationInfo("LoanRequest", "LoanTerm", "AttachedLoanTerms", true);
		LoanRequest_association_data.add(LoanRequest_associatition_AttachedLoanTerms);
		
		allassociationData.put("LoanRequest", LoanRequest_association_data);
		
		ObservableList<AssociationInfo> Loan_association_data = FXCollections.observableArrayList();
		AssociationInfo Loan_associatition_ReferedLoanRequest = new AssociationInfo("Loan", "LoanRequest", "ReferedLoanRequest", false);
		Loan_association_data.add(Loan_associatition_ReferedLoanRequest);
		AssociationInfo Loan_associatition_BelongedLoanAccount = new AssociationInfo("Loan", "LoanAccount", "BelongedLoanAccount", false);
		Loan_association_data.add(Loan_associatition_BelongedLoanAccount);
		
		allassociationData.put("Loan", Loan_association_data);
		
		ObservableList<AssociationInfo> LoanTerm_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("LoanTerm", LoanTerm_association_data);
		
		ObservableList<AssociationInfo> CheckingAccount_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("CheckingAccount", CheckingAccount_association_data);
		
		ObservableList<AssociationInfo> CreditHistory_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("CreditHistory", CreditHistory_association_data);
		
		ObservableList<AssociationInfo> LoanAccount_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("LoanAccount", LoanAccount_association_data);
		
		ObservableList<AssociationInfo> ApprovalLetter_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("ApprovalLetter", ApprovalLetter_association_data);
		
		ObservableList<AssociationInfo> LoanAgreement_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("LoanAgreement", LoanAgreement_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		definitions_map.put("enterLoanInformation", "loanrequest:LoanRequest = LoanRequest.allInstance()->any(loa:LoanRequest | loa.RequestID = requestid)\r\r\n");
		definitions_map.put("listSubmitedLoanRequest", "rs:Set(LoanRequest) = LoanRequest.allInstance()->select(r:LoanRequest | r.Status =  LoanRequestStatus :: SUBMITTED)\r\r\n");
		definitions_map.put("chooseLoanRequest", "rs:LoanRequest = self.CurrentLoanRequests->any(r:LoanRequest | r.RequestID = requestid)\r\r\n");
		definitions_map.put("listTenLoanRequest", "rs:Set(LoanRequest) = LoanRequest.allInstance()->select(r:LoanRequest | r.Status =  LoanRequestStatus :: REFERENCESVALIDATED)\r\r\n");
		definitions_map.put("chooseOneForReview", "rs:LoanRequest = self.CurrentLoanRequests->any(r:LoanRequest | r.RequestID = requestid)\r\r\n");
		definitions_map.put("addLoanTerm", "loanterm:LoanTerm = LoanTerm.allInstance()->any(loa:LoanTerm | loa.ItemID = termid)\r\r\n");
		definitions_map.put("listApprovalRequest", "rs:Set(LoanRequest) = LoanRequest.allInstance()->select(r:LoanRequest | r.Status =  LoanRequestStatus :: APPROVED)\r\r\n");
		definitions_map.put("genereateApprovalLetter", "r:LoanRequest = LoanRequest.allInstance()->any(lr:LoanRequest | lr.RequestID = id)\r\r\n");
		definitions_map.put("bookNewLoan", "loan:Loan = Loan.allInstance()->any(loa:Loan | loa.LoanID = loanid)\n\rr:LoanRequest = LoanRequest.allInstance()->any(lr:LoanRequest | lr.RequestID = requestid)\n\rla:LoanAccount = LoanAccount.allInstance()->any(lacc:LoanAccount | lacc.LoanAccountID = accountid)\r\r\n");
		definitions_map.put("generateStandardPaymentNotice", "loans:Set(Loan) = Loan.allInstance()->select(loa:Loan | loa.Status = LoanStatus :: LSOPEN and Today.After(3).isAfter(loa.CurrentOverDueDate))\r\r\n");
		definitions_map.put("generateLateNotice", "loans:Set(Loan) = Loan.allInstance()->select(loa:Loan | loa.Status = LoanStatus :: LSOPEN and Today.isAfter(loa.CurrentOverDueDate))\r\r\n");
		definitions_map.put("loanPayment", "loan:Loan = Loan.allInstance()->any(loa:Loan | loa.LoanID = loanid)\r\r\n");
		definitions_map.put("closeOutLoan", "loan:Loan = Loan.allInstance()->any(loa:Loan | loa.LoanID = loanid)\r\r\n");
		definitions_map.put("createLoanTerm", "loanterm:LoanTerm = LoanTerm.allInstance()->any(loa:LoanTerm | loa.ItemID = itemid)\r\r\n");
		definitions_map.put("queryLoanTerm", "loanterm:LoanTerm = LoanTerm.allInstance()->any(loa:LoanTerm | loa.ItemID = itemid)\r\r\n");
		definitions_map.put("modifyLoanTerm", "loanterm:LoanTerm = LoanTerm.allInstance()->any(loa:LoanTerm | loa.ItemID = itemid)\r\r\n");
		definitions_map.put("deleteLoanTerm", "loanterm:LoanTerm = LoanTerm.allInstance()->any(loa:LoanTerm | loa.ItemID = itemid)\r\r\n");
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("enterLoanInformation", "loanrequest.oclIsUndefined() = true");
		preconditions_map.put("creditRequest", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("accountStatusRequest", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("calculateScore", "self.CurrentLoanRequest.oclIsUndefined() = false and\nCurrentLoanRequest.RequestedCAHistory.oclIsUndefined() = false and\nCurrentLoanRequest.RequestedCreditHistory.oclIsUndefined() = false\n");
		preconditions_map.put("listSubmitedLoanRequest", "rs.size() > 0");
		preconditions_map.put("chooseLoanRequest", "rs.oclIsUndefined() = false");
		preconditions_map.put("markRequestValid", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("listTenLoanRequest", "rs.oclIsUndefined() = false");
		preconditions_map.put("chooseOneForReview", "rs.oclIsUndefined() = false");
		preconditions_map.put("checkCreditHistory", "self.CurrentLoanRequest.oclIsUndefined() = false and\nCurrentLoanRequest.RequestedCreditHistory.oclIsUndefined() = false\n");
		preconditions_map.put("reviewCheckingAccount", "self.CurrentLoanRequest.oclIsUndefined() = false and\nCurrentLoanRequest.RequestedCAHistory.oclIsUndefined() = false\n");
		preconditions_map.put("listAvaiableLoanTerm", "true");
		preconditions_map.put("addLoanTerm", "self.CurrentLoanRequest.oclIsUndefined() = false and\nloanterm.oclIsUndefined() = false\n");
		preconditions_map.put("approveLoanRequest", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("listApprovalRequest", "rs.oclIsUndefined() = false");
		preconditions_map.put("genereateApprovalLetter", "r.oclIsUndefined() = false");
		preconditions_map.put("emailToAppliant", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("generateLoanAgreement", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("printLoanAgreement", "self.CurrentLoanRequest.oclIsUndefined() = false");
		preconditions_map.put("bookNewLoan", "loan.oclIsUndefined() = true and\nr.oclIsUndefined() = false\n");
		preconditions_map.put("generateStandardPaymentNotice", "loans.oclIsUndefined() = false");
		preconditions_map.put("generateLateNotice", "loans.oclIsUndefined() = false");
		preconditions_map.put("loanPayment", "loan.oclIsUndefined() = false and\nloan.Status = LoanStatus::LSOPEN\n");
		preconditions_map.put("closeOutLoan", "loan.oclIsUndefined() = false and\nloan.Status = LoanStatus::LSOPEN\n");
		preconditions_map.put("createLoanTerm", "loanterm.oclIsUndefined() = true");
		preconditions_map.put("queryLoanTerm", "loanterm.oclIsUndefined() = false");
		preconditions_map.put("modifyLoanTerm", "loanterm.oclIsUndefined() = false");
		preconditions_map.put("deleteLoanTerm", "loanterm.oclIsUndefined() = false and\nLoanTerm.allInstance()->includes(loanterm)\n");
		preconditions_map.put("getCheckingAccountStatus", "true");
		preconditions_map.put("getCreditHistory", "true");
		preconditions_map.put("sendEmail", "true");
		preconditions_map.put("print", "true");
		preconditions_map.put("createLoanAccount", "true");
		preconditions_map.put("transferFunds", "true");
		preconditions_map.put("listBookedLoans", "true");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("enterLoanInformation", "let loa:LoanRequest inloa.oclIsNew() and\nloa.RequestID = requestid and\nloa.Name = name and\nloa.LoanAmount = loanamount and\nloa.LoanPurpose = loanpurpose and\nloa.Income = income and\nloa.PhoneNumber = phonenumber and\nloa.PostalAddress = postaladdress and\nloa.ZipCode = zipcode and\nloa.Email = email and\nloa.WorkReferences = workreferences and\nloa.CreditReferences = creditreferences and\nloa.CheckingAccountNumber = checkingaccountnumber and\nloa.SecurityNumber = securitynumber and\nLoanRequest.allInstance()->includes(loa) and\nself.CurrentLoanRequest = loa and\nresult = true\n");
		postconditions_map.put("creditRequest", "let his:CreditHistory inhis.oclIsNew() and\nhis = getCreditHistory(CurrentLoanRequest.SecurityNumber , CurrentLoanRequest.Name) and\nCurrentLoanRequest.RequestedCreditHistory = his and\nCreditHistory.allInstance()->includes(his) and\nresult = true\n");
		postconditions_map.put("accountStatusRequest", "let ca:CheckingAccount inca.oclIsNew() and\nca = getCheckingAccountStatus(self.CurrentLoanRequest.CheckingAccountNumber) and\nself.CurrentLoanRequest.RequestedCAHistory = ca and\nCheckingAccount.allInstance()->includes(ca) and\nresult = true\n");
		postconditions_map.put("calculateScore", "// outstandingdebt,  baddebits,  balance,  checkingaccountstatus,  income \nself.CurrentLoanRequest.CreditScore = 100 and\nself.CurrentLoanRequest.Status = LoanRequestStatus::SUBMITTED and\nresult = self.CurrentLoanRequest.CreditScore\n");
		postconditions_map.put("listSubmitedLoanRequest", "self.CurrentLoanRequests = rs and\nresult = rs\n");
		postconditions_map.put("chooseLoanRequest", "self.CurrentLoanRequest = rs and\nresult = rs\n");
		postconditions_map.put("markRequestValid", "self.CurrentLoanRequest.Status = LoanRequestStatus::REFERENCESVALIDATED and\nresult = true\n");
		postconditions_map.put("listTenLoanRequest", "self.CurrentLoanRequests = rs and\nresult = rs\n");
		postconditions_map.put("chooseOneForReview", "self.CurrentLoanRequest = rs and\nresult = rs\n");
		postconditions_map.put("checkCreditHistory", "result = CurrentLoanRequest.RequestedCreditHistory");
		postconditions_map.put("reviewCheckingAccount", "result = CurrentLoanRequest.RequestedCAHistory");
		postconditions_map.put("listAvaiableLoanTerm", "result = LoanTerm.allInstance()");
		postconditions_map.put("addLoanTerm", "CurrentLoanRequest.AttachedLoanTerms->includes(loanterm) and\nresult = true\n");
		postconditions_map.put("approveLoanRequest", "self.CurrentLoanRequest.Status = LoanRequestStatus::APPROVED and\nresult = true\n");
		postconditions_map.put("listApprovalRequest", "self.CurrentLoanRequests = rs and\nresult = rs\n");
		postconditions_map.put("genereateApprovalLetter", "let l:ApprovalLetter inl.oclIsNew() and\nl.Content = \"ApprovalLetterContent\" and\nr.AttachedApprovalLetter = l and\nself.CurrentLoanRequest = r and\nApprovalLetter.allInstance()->includes(l) and\nresult = true\n");
		postconditions_map.put("emailToAppliant", "// \"Your Loan Request was approved\"\nsendEmail(CurrentLoanRequest.Email , CurrentLoanRequest.Name , \"Your Loan Request was approved\") and\nresult = true\n");
		postconditions_map.put("generateLoanAgreement", "let la:LoanAgreement inla.oclIsNew() and\nla.Content = \"Loan Agreement\" and\nself.CurrentLoanRequest.AttachedLoanAgreement = la and\nLoanAgreement.allInstance()->includes(la) and\nresult = true\n");
		postconditions_map.put("printLoanAgreement", "print(CurrentLoanRequest.AttachedLoanAgreement.Content , number) and\nresult = true\n");
		postconditions_map.put("bookNewLoan", "let loa:Loan , lacc:LoanAccount inloa.oclIsNew() and\nloa.LoanID = loanid and\nloa.StartDate = startdate and\nloa.EndDate = enddate and\nloa.RePaymentDays = repaymentdays and\nloa.Status = LoanStatus::LSOPEN and\nloa.RepaymentAmount = r.LoanAmount and\nloa.CurrentOverDueDate = startdate.After(repaymentdays) and\nif\nla.oclIsUndefined() = true\nthen\nlacc = createLoanAccount(accountid) and\nLoanAccount.allInstance()->includes(lacc) and\nlacc.Balance = r.LoanAmount and\nloa.BelongedLoanAccount = lacc\nelse\nla.Balance = la.Balance@pre + r.LoanAmount\nendif and\ntransferFunds(accountid , r.LoanAmount) and\nloa.RemainAmountToPay = r.LoanAmount and\nLoan.allInstance()->includes(loa) and\nr.ApprovalLoan = loa and\nloa.ReferedLoanRequest = r and\nresult = true\n");
		postconditions_map.put("generateStandardPaymentNotice", "loans->forAll(l:Loan |\nsendEmail(l.ReferedLoanRequest.Email , \"OverDueSoon\" , \"You account is OverDueSoon\"))\nand\nresult = true\n");
		postconditions_map.put("generateLateNotice", "loans->forAll(l:Loan |\nsendEmail(l.ReferedLoanRequest.Email , \"OverDued\" , \"You are overdued, please repayment ASAP\"))\nand\nresult = true\n");
		postconditions_map.put("loanPayment", "loan.RemainAmountToPay = loan.RemainAmountToPay@pre - loan.RepaymentAmount and\nresult = true\n");
		postconditions_map.put("closeOutLoan", "loan.Status = LoanStatus::CLOSED and\nresult = true\n");
		postconditions_map.put("createLoanTerm", "let loa:LoanTerm inloa.oclIsNew() and\nloa.ItemID = itemid and\nloa.Content = content and\nLoanTerm.allInstance()->includes(loa) and\nresult = true\n");
		postconditions_map.put("queryLoanTerm", "result = loanterm");
		postconditions_map.put("modifyLoanTerm", "loanterm.ItemID = itemid and\nloanterm.Content = content and\nresult = true\n");
		postconditions_map.put("deleteLoanTerm", "LoanTerm.allInstance()->excludes(loanterm) and\nresult = true\n");
		postconditions_map.put("getCheckingAccountStatus", "result = null");
		postconditions_map.put("getCreditHistory", "result = null");
		postconditions_map.put("sendEmail", "result = true");
		postconditions_map.put("print", "result = true");
		postconditions_map.put("createLoanAccount", "result = null");
		postconditions_map.put("transferFunds", "result = true");
		postconditions_map.put("listBookedLoans", "result = true");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		entity_invariants_map.put("LoanRequest_UniqueRequestID"," LoanRequest . allInstance() -> isUnique ( l : LoanRequest | l . RequestID )");
		entity_invariants_map.put("LoanRequest_CreditScoreGreatAndEqualZero"," CreditScore >= 0");
		entity_invariants_map.put("Loan_UniqueLoanID"," Loan . allInstance() -> isUnique ( l : Loan | l . LoanID )");
		entity_invariants_map.put("Loan_RemainAmountToPayGreatAndEqualZero"," RemainAmountToPay >= 0");
		entity_invariants_map.put("Loan_RepaymentAmountGreatAndEqualZero"," RepaymentAmount >= 0");
		entity_invariants_map.put("Loan_RePaymentDaysGreatAndEqualZero"," RePaymentDays >= 0");
		entity_invariants_map.put("LoanTerm_UniqueLoanID"," Loan . allInstance() -> isUnique ( l : Loan | l . LoanID )");
		entity_invariants_map.put("CheckingAccount_BalanceGreatAndEqualZero"," Balance >= 0");
		entity_invariants_map.put("CreditHistory_OutstandingDebtGreatAndEqualZero"," OutstandingDebt >= 0");
		entity_invariants_map.put("CreditHistory_BadDebitsGreatAndEqualZero"," BadDebits >= 0");
		entity_invariants_map.put("LoanAccount_UniqueLoanID"," Loan . allInstance() -> isUnique ( l : Loan | l . LoanID )");
		entity_invariants_map.put("LoanAccount_BalanceGreatAndEqualZero"," Balance >= 0");
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_enterLoanInformation ====================
		 GridPane enterLoanInformation = new GridPane();
		 enterLoanInformation.setHgap(4);
		 enterLoanInformation.setVgap(6);
		 enterLoanInformation.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> enterLoanInformation_content = enterLoanInformation.getChildren();
		 Label enterLoanInformation_requestid_label = new Label("requestid:");
		 enterLoanInformation_requestid_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_requestid_label);
		 GridPane.setConstraints(enterLoanInformation_requestid_label, 0, 0);
		 
		 enterLoanInformation_requestid_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_requestid_t);
		 enterLoanInformation_requestid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_requestid_t, 1, 0);
		 Label enterLoanInformation_name_label = new Label("name:");
		 enterLoanInformation_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_name_label);
		 GridPane.setConstraints(enterLoanInformation_name_label, 0, 1);
		 
		 enterLoanInformation_name_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_name_t);
		 enterLoanInformation_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_name_t, 1, 1);
		 Label enterLoanInformation_loanamount_label = new Label("loanamount:");
		 enterLoanInformation_loanamount_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_loanamount_label);
		 GridPane.setConstraints(enterLoanInformation_loanamount_label, 0, 2);
		 
		 enterLoanInformation_loanamount_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_loanamount_t);
		 enterLoanInformation_loanamount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_loanamount_t, 1, 2);
		 Label enterLoanInformation_loanpurpose_label = new Label("loanpurpose:");
		 enterLoanInformation_loanpurpose_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_loanpurpose_label);
		 GridPane.setConstraints(enterLoanInformation_loanpurpose_label, 0, 3);
		 
		 enterLoanInformation_loanpurpose_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_loanpurpose_t);
		 enterLoanInformation_loanpurpose_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_loanpurpose_t, 1, 3);
		 Label enterLoanInformation_income_label = new Label("income:");
		 enterLoanInformation_income_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_income_label);
		 GridPane.setConstraints(enterLoanInformation_income_label, 0, 4);
		 
		 enterLoanInformation_income_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_income_t);
		 enterLoanInformation_income_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_income_t, 1, 4);
		 Label enterLoanInformation_phonenumber_label = new Label("phonenumber:");
		 enterLoanInformation_phonenumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_phonenumber_label);
		 GridPane.setConstraints(enterLoanInformation_phonenumber_label, 0, 5);
		 
		 enterLoanInformation_phonenumber_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_phonenumber_t);
		 enterLoanInformation_phonenumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_phonenumber_t, 1, 5);
		 Label enterLoanInformation_postaladdress_label = new Label("postaladdress:");
		 enterLoanInformation_postaladdress_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_postaladdress_label);
		 GridPane.setConstraints(enterLoanInformation_postaladdress_label, 0, 6);
		 
		 enterLoanInformation_postaladdress_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_postaladdress_t);
		 enterLoanInformation_postaladdress_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_postaladdress_t, 1, 6);
		 Label enterLoanInformation_zipcode_label = new Label("zipcode:");
		 enterLoanInformation_zipcode_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_zipcode_label);
		 GridPane.setConstraints(enterLoanInformation_zipcode_label, 0, 7);
		 
		 enterLoanInformation_zipcode_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_zipcode_t);
		 enterLoanInformation_zipcode_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_zipcode_t, 1, 7);
		 Label enterLoanInformation_email_label = new Label("email:");
		 enterLoanInformation_email_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_email_label);
		 GridPane.setConstraints(enterLoanInformation_email_label, 0, 8);
		 
		 enterLoanInformation_email_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_email_t);
		 enterLoanInformation_email_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_email_t, 1, 8);
		 Label enterLoanInformation_workreferences_label = new Label("workreferences:");
		 enterLoanInformation_workreferences_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_workreferences_label);
		 GridPane.setConstraints(enterLoanInformation_workreferences_label, 0, 9);
		 
		 enterLoanInformation_workreferences_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_workreferences_t);
		 enterLoanInformation_workreferences_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_workreferences_t, 1, 9);
		 Label enterLoanInformation_creditreferences_label = new Label("creditreferences:");
		 enterLoanInformation_creditreferences_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_creditreferences_label);
		 GridPane.setConstraints(enterLoanInformation_creditreferences_label, 0, 10);
		 
		 enterLoanInformation_creditreferences_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_creditreferences_t);
		 enterLoanInformation_creditreferences_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_creditreferences_t, 1, 10);
		 Label enterLoanInformation_checkingaccountnumber_label = new Label("checkingaccountnumber:");
		 enterLoanInformation_checkingaccountnumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_checkingaccountnumber_label);
		 GridPane.setConstraints(enterLoanInformation_checkingaccountnumber_label, 0, 11);
		 
		 enterLoanInformation_checkingaccountnumber_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_checkingaccountnumber_t);
		 enterLoanInformation_checkingaccountnumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_checkingaccountnumber_t, 1, 11);
		 Label enterLoanInformation_securitynumber_label = new Label("securitynumber:");
		 enterLoanInformation_securitynumber_label.setMinWidth(Region.USE_PREF_SIZE);
		 enterLoanInformation_content.add(enterLoanInformation_securitynumber_label);
		 GridPane.setConstraints(enterLoanInformation_securitynumber_label, 0, 12);
		 
		 enterLoanInformation_securitynumber_t = new TextField();
		 enterLoanInformation_content.add(enterLoanInformation_securitynumber_t);
		 enterLoanInformation_securitynumber_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(enterLoanInformation_securitynumber_t, 1, 12);
		 operationPanels.put("enterLoanInformation", enterLoanInformation);
		 
		 // ==================== GridPane_creditRequest ====================
		 GridPane creditRequest = new GridPane();
		 creditRequest.setHgap(4);
		 creditRequest.setVgap(6);
		 creditRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> creditRequest_content = creditRequest.getChildren();
		 Label creditRequest_label = new Label("This operation is no intput parameters..");
		 creditRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 creditRequest_content.add(creditRequest_label);
		 GridPane.setConstraints(creditRequest_label, 0, 0);
		 operationPanels.put("creditRequest", creditRequest);
		 
		 // ==================== GridPane_accountStatusRequest ====================
		 GridPane accountStatusRequest = new GridPane();
		 accountStatusRequest.setHgap(4);
		 accountStatusRequest.setVgap(6);
		 accountStatusRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> accountStatusRequest_content = accountStatusRequest.getChildren();
		 Label accountStatusRequest_label = new Label("This operation is no intput parameters..");
		 accountStatusRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 accountStatusRequest_content.add(accountStatusRequest_label);
		 GridPane.setConstraints(accountStatusRequest_label, 0, 0);
		 operationPanels.put("accountStatusRequest", accountStatusRequest);
		 
		 // ==================== GridPane_calculateScore ====================
		 GridPane calculateScore = new GridPane();
		 calculateScore.setHgap(4);
		 calculateScore.setVgap(6);
		 calculateScore.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> calculateScore_content = calculateScore.getChildren();
		 Label calculateScore_label = new Label("This operation is no intput parameters..");
		 calculateScore_label.setMinWidth(Region.USE_PREF_SIZE);
		 calculateScore_content.add(calculateScore_label);
		 GridPane.setConstraints(calculateScore_label, 0, 0);
		 operationPanels.put("calculateScore", calculateScore);
		 
		 // ==================== GridPane_listSubmitedLoanRequest ====================
		 GridPane listSubmitedLoanRequest = new GridPane();
		 listSubmitedLoanRequest.setHgap(4);
		 listSubmitedLoanRequest.setVgap(6);
		 listSubmitedLoanRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listSubmitedLoanRequest_content = listSubmitedLoanRequest.getChildren();
		 Label listSubmitedLoanRequest_label = new Label("This operation is no intput parameters..");
		 listSubmitedLoanRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 listSubmitedLoanRequest_content.add(listSubmitedLoanRequest_label);
		 GridPane.setConstraints(listSubmitedLoanRequest_label, 0, 0);
		 operationPanels.put("listSubmitedLoanRequest", listSubmitedLoanRequest);
		 
		 // ==================== GridPane_chooseLoanRequest ====================
		 GridPane chooseLoanRequest = new GridPane();
		 chooseLoanRequest.setHgap(4);
		 chooseLoanRequest.setVgap(6);
		 chooseLoanRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseLoanRequest_content = chooseLoanRequest.getChildren();
		 Label chooseLoanRequest_requestid_label = new Label("requestid:");
		 chooseLoanRequest_requestid_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseLoanRequest_content.add(chooseLoanRequest_requestid_label);
		 GridPane.setConstraints(chooseLoanRequest_requestid_label, 0, 0);
		 
		 chooseLoanRequest_requestid_t = new TextField();
		 chooseLoanRequest_content.add(chooseLoanRequest_requestid_t);
		 chooseLoanRequest_requestid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseLoanRequest_requestid_t, 1, 0);
		 operationPanels.put("chooseLoanRequest", chooseLoanRequest);
		 
		 // ==================== GridPane_markRequestValid ====================
		 GridPane markRequestValid = new GridPane();
		 markRequestValid.setHgap(4);
		 markRequestValid.setVgap(6);
		 markRequestValid.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> markRequestValid_content = markRequestValid.getChildren();
		 Label markRequestValid_label = new Label("This operation is no intput parameters..");
		 markRequestValid_label.setMinWidth(Region.USE_PREF_SIZE);
		 markRequestValid_content.add(markRequestValid_label);
		 GridPane.setConstraints(markRequestValid_label, 0, 0);
		 operationPanels.put("markRequestValid", markRequestValid);
		 
		 // ==================== GridPane_listTenLoanRequest ====================
		 GridPane listTenLoanRequest = new GridPane();
		 listTenLoanRequest.setHgap(4);
		 listTenLoanRequest.setVgap(6);
		 listTenLoanRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listTenLoanRequest_content = listTenLoanRequest.getChildren();
		 Label listTenLoanRequest_label = new Label("This operation is no intput parameters..");
		 listTenLoanRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 listTenLoanRequest_content.add(listTenLoanRequest_label);
		 GridPane.setConstraints(listTenLoanRequest_label, 0, 0);
		 operationPanels.put("listTenLoanRequest", listTenLoanRequest);
		 
		 // ==================== GridPane_chooseOneForReview ====================
		 GridPane chooseOneForReview = new GridPane();
		 chooseOneForReview.setHgap(4);
		 chooseOneForReview.setVgap(6);
		 chooseOneForReview.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> chooseOneForReview_content = chooseOneForReview.getChildren();
		 Label chooseOneForReview_requestid_label = new Label("requestid:");
		 chooseOneForReview_requestid_label.setMinWidth(Region.USE_PREF_SIZE);
		 chooseOneForReview_content.add(chooseOneForReview_requestid_label);
		 GridPane.setConstraints(chooseOneForReview_requestid_label, 0, 0);
		 
		 chooseOneForReview_requestid_t = new TextField();
		 chooseOneForReview_content.add(chooseOneForReview_requestid_t);
		 chooseOneForReview_requestid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(chooseOneForReview_requestid_t, 1, 0);
		 operationPanels.put("chooseOneForReview", chooseOneForReview);
		 
		 // ==================== GridPane_checkCreditHistory ====================
		 GridPane checkCreditHistory = new GridPane();
		 checkCreditHistory.setHgap(4);
		 checkCreditHistory.setVgap(6);
		 checkCreditHistory.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> checkCreditHistory_content = checkCreditHistory.getChildren();
		 Label checkCreditHistory_label = new Label("This operation is no intput parameters..");
		 checkCreditHistory_label.setMinWidth(Region.USE_PREF_SIZE);
		 checkCreditHistory_content.add(checkCreditHistory_label);
		 GridPane.setConstraints(checkCreditHistory_label, 0, 0);
		 operationPanels.put("checkCreditHistory", checkCreditHistory);
		 
		 // ==================== GridPane_reviewCheckingAccount ====================
		 GridPane reviewCheckingAccount = new GridPane();
		 reviewCheckingAccount.setHgap(4);
		 reviewCheckingAccount.setVgap(6);
		 reviewCheckingAccount.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> reviewCheckingAccount_content = reviewCheckingAccount.getChildren();
		 Label reviewCheckingAccount_label = new Label("This operation is no intput parameters..");
		 reviewCheckingAccount_label.setMinWidth(Region.USE_PREF_SIZE);
		 reviewCheckingAccount_content.add(reviewCheckingAccount_label);
		 GridPane.setConstraints(reviewCheckingAccount_label, 0, 0);
		 operationPanels.put("reviewCheckingAccount", reviewCheckingAccount);
		 
		 // ==================== GridPane_listAvaiableLoanTerm ====================
		 GridPane listAvaiableLoanTerm = new GridPane();
		 listAvaiableLoanTerm.setHgap(4);
		 listAvaiableLoanTerm.setVgap(6);
		 listAvaiableLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listAvaiableLoanTerm_content = listAvaiableLoanTerm.getChildren();
		 Label listAvaiableLoanTerm_label = new Label("This operation is no intput parameters..");
		 listAvaiableLoanTerm_label.setMinWidth(Region.USE_PREF_SIZE);
		 listAvaiableLoanTerm_content.add(listAvaiableLoanTerm_label);
		 GridPane.setConstraints(listAvaiableLoanTerm_label, 0, 0);
		 operationPanels.put("listAvaiableLoanTerm", listAvaiableLoanTerm);
		 
		 // ==================== GridPane_addLoanTerm ====================
		 GridPane addLoanTerm = new GridPane();
		 addLoanTerm.setHgap(4);
		 addLoanTerm.setVgap(6);
		 addLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> addLoanTerm_content = addLoanTerm.getChildren();
		 Label addLoanTerm_termid_label = new Label("termid:");
		 addLoanTerm_termid_label.setMinWidth(Region.USE_PREF_SIZE);
		 addLoanTerm_content.add(addLoanTerm_termid_label);
		 GridPane.setConstraints(addLoanTerm_termid_label, 0, 0);
		 
		 addLoanTerm_termid_t = new TextField();
		 addLoanTerm_content.add(addLoanTerm_termid_t);
		 addLoanTerm_termid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(addLoanTerm_termid_t, 1, 0);
		 operationPanels.put("addLoanTerm", addLoanTerm);
		 
		 // ==================== GridPane_approveLoanRequest ====================
		 GridPane approveLoanRequest = new GridPane();
		 approveLoanRequest.setHgap(4);
		 approveLoanRequest.setVgap(6);
		 approveLoanRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> approveLoanRequest_content = approveLoanRequest.getChildren();
		 Label approveLoanRequest_label = new Label("This operation is no intput parameters..");
		 approveLoanRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 approveLoanRequest_content.add(approveLoanRequest_label);
		 GridPane.setConstraints(approveLoanRequest_label, 0, 0);
		 operationPanels.put("approveLoanRequest", approveLoanRequest);
		 
		 // ==================== GridPane_listApprovalRequest ====================
		 GridPane listApprovalRequest = new GridPane();
		 listApprovalRequest.setHgap(4);
		 listApprovalRequest.setVgap(6);
		 listApprovalRequest.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listApprovalRequest_content = listApprovalRequest.getChildren();
		 Label listApprovalRequest_label = new Label("This operation is no intput parameters..");
		 listApprovalRequest_label.setMinWidth(Region.USE_PREF_SIZE);
		 listApprovalRequest_content.add(listApprovalRequest_label);
		 GridPane.setConstraints(listApprovalRequest_label, 0, 0);
		 operationPanels.put("listApprovalRequest", listApprovalRequest);
		 
		 // ==================== GridPane_genereateApprovalLetter ====================
		 GridPane genereateApprovalLetter = new GridPane();
		 genereateApprovalLetter.setHgap(4);
		 genereateApprovalLetter.setVgap(6);
		 genereateApprovalLetter.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> genereateApprovalLetter_content = genereateApprovalLetter.getChildren();
		 Label genereateApprovalLetter_id_label = new Label("id:");
		 genereateApprovalLetter_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 genereateApprovalLetter_content.add(genereateApprovalLetter_id_label);
		 GridPane.setConstraints(genereateApprovalLetter_id_label, 0, 0);
		 
		 genereateApprovalLetter_id_t = new TextField();
		 genereateApprovalLetter_content.add(genereateApprovalLetter_id_t);
		 genereateApprovalLetter_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(genereateApprovalLetter_id_t, 1, 0);
		 operationPanels.put("genereateApprovalLetter", genereateApprovalLetter);
		 
		 // ==================== GridPane_emailToAppliant ====================
		 GridPane emailToAppliant = new GridPane();
		 emailToAppliant.setHgap(4);
		 emailToAppliant.setVgap(6);
		 emailToAppliant.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> emailToAppliant_content = emailToAppliant.getChildren();
		 Label emailToAppliant_label = new Label("This operation is no intput parameters..");
		 emailToAppliant_label.setMinWidth(Region.USE_PREF_SIZE);
		 emailToAppliant_content.add(emailToAppliant_label);
		 GridPane.setConstraints(emailToAppliant_label, 0, 0);
		 operationPanels.put("emailToAppliant", emailToAppliant);
		 
		 // ==================== GridPane_generateLoanAgreement ====================
		 GridPane generateLoanAgreement = new GridPane();
		 generateLoanAgreement.setHgap(4);
		 generateLoanAgreement.setVgap(6);
		 generateLoanAgreement.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> generateLoanAgreement_content = generateLoanAgreement.getChildren();
		 Label generateLoanAgreement_label = new Label("This operation is no intput parameters..");
		 generateLoanAgreement_label.setMinWidth(Region.USE_PREF_SIZE);
		 generateLoanAgreement_content.add(generateLoanAgreement_label);
		 GridPane.setConstraints(generateLoanAgreement_label, 0, 0);
		 operationPanels.put("generateLoanAgreement", generateLoanAgreement);
		 
		 // ==================== GridPane_printLoanAgreement ====================
		 GridPane printLoanAgreement = new GridPane();
		 printLoanAgreement.setHgap(4);
		 printLoanAgreement.setVgap(6);
		 printLoanAgreement.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> printLoanAgreement_content = printLoanAgreement.getChildren();
		 Label printLoanAgreement_number_label = new Label("number:");
		 printLoanAgreement_number_label.setMinWidth(Region.USE_PREF_SIZE);
		 printLoanAgreement_content.add(printLoanAgreement_number_label);
		 GridPane.setConstraints(printLoanAgreement_number_label, 0, 0);
		 
		 printLoanAgreement_number_t = new TextField();
		 printLoanAgreement_content.add(printLoanAgreement_number_t);
		 printLoanAgreement_number_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(printLoanAgreement_number_t, 1, 0);
		 operationPanels.put("printLoanAgreement", printLoanAgreement);
		 
		 // ==================== GridPane_bookNewLoan ====================
		 GridPane bookNewLoan = new GridPane();
		 bookNewLoan.setHgap(4);
		 bookNewLoan.setVgap(6);
		 bookNewLoan.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> bookNewLoan_content = bookNewLoan.getChildren();
		 Label bookNewLoan_requestid_label = new Label("requestid:");
		 bookNewLoan_requestid_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_requestid_label);
		 GridPane.setConstraints(bookNewLoan_requestid_label, 0, 0);
		 
		 bookNewLoan_requestid_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_requestid_t);
		 bookNewLoan_requestid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_requestid_t, 1, 0);
		 Label bookNewLoan_loanid_label = new Label("loanid:");
		 bookNewLoan_loanid_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_loanid_label);
		 GridPane.setConstraints(bookNewLoan_loanid_label, 0, 1);
		 
		 bookNewLoan_loanid_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_loanid_t);
		 bookNewLoan_loanid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_loanid_t, 1, 1);
		 Label bookNewLoan_accountid_label = new Label("accountid:");
		 bookNewLoan_accountid_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_accountid_label);
		 GridPane.setConstraints(bookNewLoan_accountid_label, 0, 2);
		 
		 bookNewLoan_accountid_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_accountid_t);
		 bookNewLoan_accountid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_accountid_t, 1, 2);
		 Label bookNewLoan_startdate_label = new Label("startdate (yyyy-MM-dd):");
		 bookNewLoan_startdate_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_startdate_label);
		 GridPane.setConstraints(bookNewLoan_startdate_label, 0, 3);
		 
		 bookNewLoan_startdate_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_startdate_t);
		 bookNewLoan_startdate_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_startdate_t, 1, 3);
		 Label bookNewLoan_enddate_label = new Label("enddate (yyyy-MM-dd):");
		 bookNewLoan_enddate_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_enddate_label);
		 GridPane.setConstraints(bookNewLoan_enddate_label, 0, 4);
		 
		 bookNewLoan_enddate_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_enddate_t);
		 bookNewLoan_enddate_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_enddate_t, 1, 4);
		 Label bookNewLoan_repaymentdays_label = new Label("repaymentdays:");
		 bookNewLoan_repaymentdays_label.setMinWidth(Region.USE_PREF_SIZE);
		 bookNewLoan_content.add(bookNewLoan_repaymentdays_label);
		 GridPane.setConstraints(bookNewLoan_repaymentdays_label, 0, 5);
		 
		 bookNewLoan_repaymentdays_t = new TextField();
		 bookNewLoan_content.add(bookNewLoan_repaymentdays_t);
		 bookNewLoan_repaymentdays_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(bookNewLoan_repaymentdays_t, 1, 5);
		 operationPanels.put("bookNewLoan", bookNewLoan);
		 
		 // ==================== GridPane_generateStandardPaymentNotice ====================
		 GridPane generateStandardPaymentNotice = new GridPane();
		 generateStandardPaymentNotice.setHgap(4);
		 generateStandardPaymentNotice.setVgap(6);
		 generateStandardPaymentNotice.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> generateStandardPaymentNotice_content = generateStandardPaymentNotice.getChildren();
		 Label generateStandardPaymentNotice_label = new Label("This operation is no intput parameters..");
		 generateStandardPaymentNotice_label.setMinWidth(Region.USE_PREF_SIZE);
		 generateStandardPaymentNotice_content.add(generateStandardPaymentNotice_label);
		 GridPane.setConstraints(generateStandardPaymentNotice_label, 0, 0);
		 operationPanels.put("generateStandardPaymentNotice", generateStandardPaymentNotice);
		 
		 // ==================== GridPane_generateLateNotice ====================
		 GridPane generateLateNotice = new GridPane();
		 generateLateNotice.setHgap(4);
		 generateLateNotice.setVgap(6);
		 generateLateNotice.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> generateLateNotice_content = generateLateNotice.getChildren();
		 Label generateLateNotice_label = new Label("This operation is no intput parameters..");
		 generateLateNotice_label.setMinWidth(Region.USE_PREF_SIZE);
		 generateLateNotice_content.add(generateLateNotice_label);
		 GridPane.setConstraints(generateLateNotice_label, 0, 0);
		 operationPanels.put("generateLateNotice", generateLateNotice);
		 
		 // ==================== GridPane_loanPayment ====================
		 GridPane loanPayment = new GridPane();
		 loanPayment.setHgap(4);
		 loanPayment.setVgap(6);
		 loanPayment.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> loanPayment_content = loanPayment.getChildren();
		 Label loanPayment_loanid_label = new Label("loanid:");
		 loanPayment_loanid_label.setMinWidth(Region.USE_PREF_SIZE);
		 loanPayment_content.add(loanPayment_loanid_label);
		 GridPane.setConstraints(loanPayment_loanid_label, 0, 0);
		 
		 loanPayment_loanid_t = new TextField();
		 loanPayment_content.add(loanPayment_loanid_t);
		 loanPayment_loanid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(loanPayment_loanid_t, 1, 0);
		 operationPanels.put("loanPayment", loanPayment);
		 
		 // ==================== GridPane_closeOutLoan ====================
		 GridPane closeOutLoan = new GridPane();
		 closeOutLoan.setHgap(4);
		 closeOutLoan.setVgap(6);
		 closeOutLoan.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> closeOutLoan_content = closeOutLoan.getChildren();
		 Label closeOutLoan_loanid_label = new Label("loanid:");
		 closeOutLoan_loanid_label.setMinWidth(Region.USE_PREF_SIZE);
		 closeOutLoan_content.add(closeOutLoan_loanid_label);
		 GridPane.setConstraints(closeOutLoan_loanid_label, 0, 0);
		 
		 closeOutLoan_loanid_t = new TextField();
		 closeOutLoan_content.add(closeOutLoan_loanid_t);
		 closeOutLoan_loanid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(closeOutLoan_loanid_t, 1, 0);
		 operationPanels.put("closeOutLoan", closeOutLoan);
		 
		 // ==================== GridPane_createLoanTerm ====================
		 GridPane createLoanTerm = new GridPane();
		 createLoanTerm.setHgap(4);
		 createLoanTerm.setVgap(6);
		 createLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createLoanTerm_content = createLoanTerm.getChildren();
		 Label createLoanTerm_itemid_label = new Label("itemid:");
		 createLoanTerm_itemid_label.setMinWidth(Region.USE_PREF_SIZE);
		 createLoanTerm_content.add(createLoanTerm_itemid_label);
		 GridPane.setConstraints(createLoanTerm_itemid_label, 0, 0);
		 
		 createLoanTerm_itemid_t = new TextField();
		 createLoanTerm_content.add(createLoanTerm_itemid_t);
		 createLoanTerm_itemid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createLoanTerm_itemid_t, 1, 0);
		 Label createLoanTerm_content_label = new Label("content:");
		 createLoanTerm_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 createLoanTerm_content.add(createLoanTerm_content_label);
		 GridPane.setConstraints(createLoanTerm_content_label, 0, 1);
		 
		 createLoanTerm_content_t = new TextField();
		 createLoanTerm_content.add(createLoanTerm_content_t);
		 createLoanTerm_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createLoanTerm_content_t, 1, 1);
		 operationPanels.put("createLoanTerm", createLoanTerm);
		 
		 // ==================== GridPane_queryLoanTerm ====================
		 GridPane queryLoanTerm = new GridPane();
		 queryLoanTerm.setHgap(4);
		 queryLoanTerm.setVgap(6);
		 queryLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> queryLoanTerm_content = queryLoanTerm.getChildren();
		 Label queryLoanTerm_itemid_label = new Label("itemid:");
		 queryLoanTerm_itemid_label.setMinWidth(Region.USE_PREF_SIZE);
		 queryLoanTerm_content.add(queryLoanTerm_itemid_label);
		 GridPane.setConstraints(queryLoanTerm_itemid_label, 0, 0);
		 
		 queryLoanTerm_itemid_t = new TextField();
		 queryLoanTerm_content.add(queryLoanTerm_itemid_t);
		 queryLoanTerm_itemid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(queryLoanTerm_itemid_t, 1, 0);
		 operationPanels.put("queryLoanTerm", queryLoanTerm);
		 
		 // ==================== GridPane_modifyLoanTerm ====================
		 GridPane modifyLoanTerm = new GridPane();
		 modifyLoanTerm.setHgap(4);
		 modifyLoanTerm.setVgap(6);
		 modifyLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> modifyLoanTerm_content = modifyLoanTerm.getChildren();
		 Label modifyLoanTerm_itemid_label = new Label("itemid:");
		 modifyLoanTerm_itemid_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyLoanTerm_content.add(modifyLoanTerm_itemid_label);
		 GridPane.setConstraints(modifyLoanTerm_itemid_label, 0, 0);
		 
		 modifyLoanTerm_itemid_t = new TextField();
		 modifyLoanTerm_content.add(modifyLoanTerm_itemid_t);
		 modifyLoanTerm_itemid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyLoanTerm_itemid_t, 1, 0);
		 Label modifyLoanTerm_content_label = new Label("content:");
		 modifyLoanTerm_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 modifyLoanTerm_content.add(modifyLoanTerm_content_label);
		 GridPane.setConstraints(modifyLoanTerm_content_label, 0, 1);
		 
		 modifyLoanTerm_content_t = new TextField();
		 modifyLoanTerm_content.add(modifyLoanTerm_content_t);
		 modifyLoanTerm_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(modifyLoanTerm_content_t, 1, 1);
		 operationPanels.put("modifyLoanTerm", modifyLoanTerm);
		 
		 // ==================== GridPane_deleteLoanTerm ====================
		 GridPane deleteLoanTerm = new GridPane();
		 deleteLoanTerm.setHgap(4);
		 deleteLoanTerm.setVgap(6);
		 deleteLoanTerm.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> deleteLoanTerm_content = deleteLoanTerm.getChildren();
		 Label deleteLoanTerm_itemid_label = new Label("itemid:");
		 deleteLoanTerm_itemid_label.setMinWidth(Region.USE_PREF_SIZE);
		 deleteLoanTerm_content.add(deleteLoanTerm_itemid_label);
		 GridPane.setConstraints(deleteLoanTerm_itemid_label, 0, 0);
		 
		 deleteLoanTerm_itemid_t = new TextField();
		 deleteLoanTerm_content.add(deleteLoanTerm_itemid_t);
		 deleteLoanTerm_itemid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(deleteLoanTerm_itemid_t, 1, 0);
		 operationPanels.put("deleteLoanTerm", deleteLoanTerm);
		 
		 // ==================== GridPane_getCheckingAccountStatus ====================
		 GridPane getCheckingAccountStatus = new GridPane();
		 getCheckingAccountStatus.setHgap(4);
		 getCheckingAccountStatus.setVgap(6);
		 getCheckingAccountStatus.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getCheckingAccountStatus_content = getCheckingAccountStatus.getChildren();
		 Label getCheckingAccountStatus_cid_label = new Label("cid:");
		 getCheckingAccountStatus_cid_label.setMinWidth(Region.USE_PREF_SIZE);
		 getCheckingAccountStatus_content.add(getCheckingAccountStatus_cid_label);
		 GridPane.setConstraints(getCheckingAccountStatus_cid_label, 0, 0);
		 
		 getCheckingAccountStatus_cid_t = new TextField();
		 getCheckingAccountStatus_content.add(getCheckingAccountStatus_cid_t);
		 getCheckingAccountStatus_cid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getCheckingAccountStatus_cid_t, 1, 0);
		 operationPanels.put("getCheckingAccountStatus", getCheckingAccountStatus);
		 
		 // ==================== GridPane_getCreditHistory ====================
		 GridPane getCreditHistory = new GridPane();
		 getCreditHistory.setHgap(4);
		 getCreditHistory.setVgap(6);
		 getCreditHistory.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> getCreditHistory_content = getCreditHistory.getChildren();
		 Label getCreditHistory_securityid_label = new Label("securityid:");
		 getCreditHistory_securityid_label.setMinWidth(Region.USE_PREF_SIZE);
		 getCreditHistory_content.add(getCreditHistory_securityid_label);
		 GridPane.setConstraints(getCreditHistory_securityid_label, 0, 0);
		 
		 getCreditHistory_securityid_t = new TextField();
		 getCreditHistory_content.add(getCreditHistory_securityid_t);
		 getCreditHistory_securityid_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getCreditHistory_securityid_t, 1, 0);
		 Label getCreditHistory_name_label = new Label("name:");
		 getCreditHistory_name_label.setMinWidth(Region.USE_PREF_SIZE);
		 getCreditHistory_content.add(getCreditHistory_name_label);
		 GridPane.setConstraints(getCreditHistory_name_label, 0, 1);
		 
		 getCreditHistory_name_t = new TextField();
		 getCreditHistory_content.add(getCreditHistory_name_t);
		 getCreditHistory_name_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(getCreditHistory_name_t, 1, 1);
		 operationPanels.put("getCreditHistory", getCreditHistory);
		 
		 // ==================== GridPane_sendEmail ====================
		 GridPane sendEmail = new GridPane();
		 sendEmail.setHgap(4);
		 sendEmail.setVgap(6);
		 sendEmail.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> sendEmail_content = sendEmail.getChildren();
		 Label sendEmail_emailaddress_label = new Label("emailaddress:");
		 sendEmail_emailaddress_label.setMinWidth(Region.USE_PREF_SIZE);
		 sendEmail_content.add(sendEmail_emailaddress_label);
		 GridPane.setConstraints(sendEmail_emailaddress_label, 0, 0);
		 
		 sendEmail_emailaddress_t = new TextField();
		 sendEmail_content.add(sendEmail_emailaddress_t);
		 sendEmail_emailaddress_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(sendEmail_emailaddress_t, 1, 0);
		 Label sendEmail_title_label = new Label("title:");
		 sendEmail_title_label.setMinWidth(Region.USE_PREF_SIZE);
		 sendEmail_content.add(sendEmail_title_label);
		 GridPane.setConstraints(sendEmail_title_label, 0, 1);
		 
		 sendEmail_title_t = new TextField();
		 sendEmail_content.add(sendEmail_title_t);
		 sendEmail_title_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(sendEmail_title_t, 1, 1);
		 Label sendEmail_content_label = new Label("content:");
		 sendEmail_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 sendEmail_content.add(sendEmail_content_label);
		 GridPane.setConstraints(sendEmail_content_label, 0, 2);
		 
		 sendEmail_content_t = new TextField();
		 sendEmail_content.add(sendEmail_content_t);
		 sendEmail_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(sendEmail_content_t, 1, 2);
		 operationPanels.put("sendEmail", sendEmail);
		 
		 // ==================== GridPane_print ====================
		 GridPane print = new GridPane();
		 print.setHgap(4);
		 print.setVgap(6);
		 print.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> print_content = print.getChildren();
		 Label print_content_label = new Label("content:");
		 print_content_label.setMinWidth(Region.USE_PREF_SIZE);
		 print_content.add(print_content_label);
		 GridPane.setConstraints(print_content_label, 0, 0);
		 
		 print_content_t = new TextField();
		 print_content.add(print_content_t);
		 print_content_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(print_content_t, 1, 0);
		 Label print_numbers_label = new Label("numbers:");
		 print_numbers_label.setMinWidth(Region.USE_PREF_SIZE);
		 print_content.add(print_numbers_label);
		 GridPane.setConstraints(print_numbers_label, 0, 1);
		 
		 print_numbers_t = new TextField();
		 print_content.add(print_numbers_t);
		 print_numbers_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(print_numbers_t, 1, 1);
		 operationPanels.put("print", print);
		 
		 // ==================== GridPane_createLoanAccount ====================
		 GridPane createLoanAccount = new GridPane();
		 createLoanAccount.setHgap(4);
		 createLoanAccount.setVgap(6);
		 createLoanAccount.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> createLoanAccount_content = createLoanAccount.getChildren();
		 Label createLoanAccount_id_label = new Label("id:");
		 createLoanAccount_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 createLoanAccount_content.add(createLoanAccount_id_label);
		 GridPane.setConstraints(createLoanAccount_id_label, 0, 0);
		 
		 createLoanAccount_id_t = new TextField();
		 createLoanAccount_content.add(createLoanAccount_id_t);
		 createLoanAccount_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(createLoanAccount_id_t, 1, 0);
		 operationPanels.put("createLoanAccount", createLoanAccount);
		 
		 // ==================== GridPane_transferFunds ====================
		 GridPane transferFunds = new GridPane();
		 transferFunds.setHgap(4);
		 transferFunds.setVgap(6);
		 transferFunds.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> transferFunds_content = transferFunds.getChildren();
		 Label transferFunds_id_label = new Label("id:");
		 transferFunds_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 transferFunds_content.add(transferFunds_id_label);
		 GridPane.setConstraints(transferFunds_id_label, 0, 0);
		 
		 transferFunds_id_t = new TextField();
		 transferFunds_content.add(transferFunds_id_t);
		 transferFunds_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(transferFunds_id_t, 1, 0);
		 Label transferFunds_amount_label = new Label("amount:");
		 transferFunds_amount_label.setMinWidth(Region.USE_PREF_SIZE);
		 transferFunds_content.add(transferFunds_amount_label);
		 GridPane.setConstraints(transferFunds_amount_label, 0, 1);
		 
		 transferFunds_amount_t = new TextField();
		 transferFunds_content.add(transferFunds_amount_t);
		 transferFunds_amount_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(transferFunds_amount_t, 1, 1);
		 operationPanels.put("transferFunds", transferFunds);
		 
		 // ==================== GridPane_listBookedLoans ====================
		 GridPane listBookedLoans = new GridPane();
		 listBookedLoans.setHgap(4);
		 listBookedLoans.setVgap(6);
		 listBookedLoans.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> listBookedLoans_content = listBookedLoans.getChildren();
		 Label listBookedLoans_label = new Label("This operation is no intput parameters..");
		 listBookedLoans_label.setMinWidth(Region.USE_PREF_SIZE);
		 listBookedLoans_content.add(listBookedLoans_label);
		 GridPane.setConstraints(listBookedLoans_label, 0, 0);
		 operationPanels.put("listBookedLoans", listBookedLoans);
		 
	}	

	public void actorTreeViewBinding() {
		
		TreeItem<String> treeRootloanofficer = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_evaluateLoanRequest = new TreeItem<String>("evaluateLoanRequest");
			subTreeRoot_evaluateLoanRequest.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("listTenLoanRequest"),
					 	new TreeItem<String>("chooseOneForReview"),
					 	new TreeItem<String>("checkCreditHistory"),
					 	new TreeItem<String>("reviewCheckingAccount"),
					 	new TreeItem<String>("listAvaiableLoanTerm"),
					 	new TreeItem<String>("addLoanTerm"),
					 	new TreeItem<String>("approveLoanRequest")
				 		));	
			TreeItem<String> subTreeRoot_manageLoanTerm = new TreeItem<String>("manageLoanTerm");
			subTreeRoot_manageLoanTerm.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("createLoanTerm"),
					 	new TreeItem<String>("queryLoanTerm"),
					 	new TreeItem<String>("modifyLoanTerm"),
					 	new TreeItem<String>("deleteLoanTerm")
				 		));	
		
		treeRootloanofficer.getChildren().addAll(Arrays.asList(
			subTreeRoot_evaluateLoanRequest,
			subTreeRoot_manageLoanTerm
			));
		
		treeRootloanofficer.setExpanded(true);

		actor_treeview_loanofficer.setShowRoot(false);
		actor_treeview_loanofficer.setRoot(treeRootloanofficer);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_loanofficer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootloanassistant = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_enterValidatedCreditReferences = new TreeItem<String>("enterValidatedCreditReferences");
			subTreeRoot_enterValidatedCreditReferences.getChildren().addAll(Arrays.asList(
				 	new TreeItem<String>("listSubmitedLoanRequest"),
				 	new TreeItem<String>("chooseLoanRequest"),
				 	new TreeItem<String>("markRequestValid")
				 	));
				
		
		treeRootloanassistant.getChildren().addAll(Arrays.asList(
			subTreeRoot_enterValidatedCreditReferences
			));
		
		treeRootloanassistant.setExpanded(true);

		actor_treeview_loanassistant.setShowRoot(false);
		actor_treeview_loanassistant.setRoot(treeRootloanassistant);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_loanassistant.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootloanclerk = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_generateLoanLetterAndAgreement = new TreeItem<String>("generateLoanLetterAndAgreement");
			subTreeRoot_generateLoanLetterAndAgreement.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("listApprovalRequest"),
					 	new TreeItem<String>("genereateApprovalLetter"),
					 	new TreeItem<String>("emailToAppliant"),
					 	new TreeItem<String>("generateLoanAgreement"),
					 	new TreeItem<String>("printLoanAgreement")
				 		));	
		
		treeRootloanclerk.getChildren().addAll(Arrays.asList(
			subTreeRoot_generateLoanLetterAndAgreement,
			new TreeItem<String>("bookNewLoan"),
			new TreeItem<String>("loanPayment"),
			new TreeItem<String>("closeOutLoan")
			));
		
		treeRootloanclerk.setExpanded(true);

		actor_treeview_loanclerk.setShowRoot(false);
		actor_treeview_loanclerk.setRoot(treeRootloanclerk);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_loanclerk.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootscheduler = new TreeItem<String>("Root node");
		
		treeRootscheduler.getChildren().addAll(Arrays.asList(
			new TreeItem<String>("generateStandardPaymentNotice"),
			new TreeItem<String>("generateLateNotice")
			));
		
		treeRootscheduler.setExpanded(true);

		actor_treeview_scheduler.setShowRoot(false);
		actor_treeview_scheduler.setRoot(treeRootscheduler);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_scheduler.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootapplicant = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_submitLoanRequest = new TreeItem<String>("submitLoanRequest");
			subTreeRoot_submitLoanRequest.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("enterLoanInformation"),
					 	new TreeItem<String>("creditRequest"),
					 	new TreeItem<String>("accountStatusRequest"),
					 	new TreeItem<String>("calculateScore")
				 		));	
		
		treeRootapplicant.getChildren().addAll(Arrays.asList(
			subTreeRoot_submitLoanRequest
			));
		
		treeRootapplicant.setExpanded(true);

		actor_treeview_applicant.setShowRoot(false);
		actor_treeview_applicant.setRoot(treeRootapplicant);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_applicant.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "enterLoanInformation" : enterLoanInformation(); break;
		case "creditRequest" : creditRequest(); break;
		case "accountStatusRequest" : accountStatusRequest(); break;
		case "calculateScore" : calculateScore(); break;
		case "listSubmitedLoanRequest" : listSubmitedLoanRequest(); break;
		case "chooseLoanRequest" : chooseLoanRequest(); break;
		case "markRequestValid" : markRequestValid(); break;
		case "listTenLoanRequest" : listTenLoanRequest(); break;
		case "chooseOneForReview" : chooseOneForReview(); break;
		case "checkCreditHistory" : checkCreditHistory(); break;
		case "reviewCheckingAccount" : reviewCheckingAccount(); break;
		case "listAvaiableLoanTerm" : listAvaiableLoanTerm(); break;
		case "addLoanTerm" : addLoanTerm(); break;
		case "approveLoanRequest" : approveLoanRequest(); break;
		case "listApprovalRequest" : listApprovalRequest(); break;
		case "genereateApprovalLetter" : genereateApprovalLetter(); break;
		case "emailToAppliant" : emailToAppliant(); break;
		case "generateLoanAgreement" : generateLoanAgreement(); break;
		case "printLoanAgreement" : printLoanAgreement(); break;
		case "bookNewLoan" : bookNewLoan(); break;
		case "generateStandardPaymentNotice" : generateStandardPaymentNotice(); break;
		case "generateLateNotice" : generateLateNotice(); break;
		case "loanPayment" : loanPayment(); break;
		case "closeOutLoan" : closeOutLoan(); break;
		case "createLoanTerm" : createLoanTerm(); break;
		case "queryLoanTerm" : queryLoanTerm(); break;
		case "modifyLoanTerm" : modifyLoanTerm(); break;
		case "deleteLoanTerm" : deleteLoanTerm(); break;
		case "getCheckingAccountStatus" : getCheckingAccountStatus(); break;
		case "getCreditHistory" : getCreditHistory(); break;
		case "sendEmail" : sendEmail(); break;
		case "print" : print(); break;
		case "createLoanAccount" : createLoanAccount(); break;
		case "transferFunds" : transferFunds(); break;
		case "listBookedLoans" : listBookedLoans(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void enterLoanInformation() {
		
		System.out.println("execute enterLoanInformation");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: enterLoanInformation in service: SubmitLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(submitloanrequestmodule_service.enterLoanInformation(
			Integer.valueOf(enterLoanInformation_requestid_t.getText()),
			enterLoanInformation_name_t.getText(),
			Float.valueOf(enterLoanInformation_loanamount_t.getText()),
			enterLoanInformation_loanpurpose_t.getText(),
			Float.valueOf(enterLoanInformation_income_t.getText()),
			Integer.valueOf(enterLoanInformation_phonenumber_t.getText()),
			enterLoanInformation_postaladdress_t.getText(),
			Integer.valueOf(enterLoanInformation_zipcode_t.getText()),
			enterLoanInformation_email_t.getText(),
			enterLoanInformation_workreferences_t.getText(),
			enterLoanInformation_creditreferences_t.getText(),
			Integer.valueOf(enterLoanInformation_checkingaccountnumber_t.getText()),
			Integer.valueOf(enterLoanInformation_securitynumber_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void creditRequest() {
		
		System.out.println("execute creditRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: creditRequest in service: SubmitLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(submitloanrequestmodule_service.creditRequest(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void accountStatusRequest() {
		
		System.out.println("execute accountStatusRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: accountStatusRequest in service: SubmitLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(submitloanrequestmodule_service.accountStatusRequest(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void calculateScore() {
		
		System.out.println("execute calculateScore");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: calculateScore in service: SubmitLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(submitloanrequestmodule_service.calculateScore(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listSubmitedLoanRequest() {
		
		System.out.println("execute listSubmitedLoanRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listSubmitedLoanRequest in service: EnterValidatedCreditReferencesModule ");
		
		try {
			//invoke op with parameters
					List<LoanRequest> result = entervalidatedcreditreferencesmodule_service.listSubmitedLoanRequest(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
					tableLoanRequest_Status.setMinWidth("Status".length()*10);
					tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Status);
					TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
					tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
					tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
					TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
					tableLoanRequest_Name.setMinWidth("Name".length()*10);
					tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Name);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
					tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
					tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
					tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
					tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
					TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
					tableLoanRequest_Income.setMinWidth("Income".length()*10);
					tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Income);
					TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
					tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
					tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
					tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
					tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
					TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
					tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
					tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
					TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
					tableLoanRequest_Email.setMinWidth("Email".length()*10);
					tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Email);
					TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
					tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
					tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
					tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
					tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
					tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
					tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
					tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
					tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
					tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
					tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
					
					ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
					for (LoanRequest r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Status", String.valueOf(r.getStatus()));
						unit.put("RequestID", String.valueOf(r.getRequestID()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
						if (r.getLoanPurpose() != null)
							unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
						else
							unit.put("LoanPurpose", "");
						unit.put("Income", String.valueOf(r.getIncome()));
						unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
						if (r.getPostalAddress() != null)
							unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
						else
							unit.put("PostalAddress", "");
						unit.put("ZipCode", String.valueOf(r.getZipCode()));
						if (r.getEmail() != null)
							unit.put("Email", String.valueOf(r.getEmail()));
						else
							unit.put("Email", "");
						if (r.getWorkReferences() != null)
							unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
						else
							unit.put("WorkReferences", "");
						if (r.getCreditReferences() != null)
							unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
						else
							unit.put("CreditReferences", "");
						unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
						unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
						unit.put("CreditScore", String.valueOf(r.getCreditScore()));
						dataLoanRequest.add(unit);
					}
					
					tableLoanRequest.setItems(dataLoanRequest);
					operation_return_pane.setContent(tableLoanRequest);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseLoanRequest() {
		
		System.out.println("execute chooseLoanRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseLoanRequest in service: EnterValidatedCreditReferencesModule ");
		
		try {
			//invoke op with parameters
				LoanRequest r = entervalidatedcreditreferencesmodule_service.chooseLoanRequest(
				Integer.valueOf(chooseLoanRequest_requestid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
				tableLoanRequest_Status.setMinWidth("Status".length()*10);
				tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Status);
				TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
				tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
				tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
				TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
				tableLoanRequest_Name.setMinWidth("Name".length()*10);
				tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Name);
				TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
				tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
				tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
				TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
				tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
				tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
				TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
				tableLoanRequest_Income.setMinWidth("Income".length()*10);
				tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Income);
				TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
				tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
				tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
				tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
				tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
				TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
				tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
				tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
				TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
				tableLoanRequest_Email.setMinWidth("Email".length()*10);
				tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Email);
				TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
				tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
				tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
				TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
				tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
				tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
				TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
				tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
				tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
				tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
				tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
				tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
				tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
				
				ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Status", String.valueOf(r.getStatus()));
					unit.put("RequestID", String.valueOf(r.getRequestID()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
					if (r.getLoanPurpose() != null)
						unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
					else
						unit.put("LoanPurpose", "");
					unit.put("Income", String.valueOf(r.getIncome()));
					unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
					if (r.getPostalAddress() != null)
						unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
					else
						unit.put("PostalAddress", "");
					unit.put("ZipCode", String.valueOf(r.getZipCode()));
					if (r.getEmail() != null)
						unit.put("Email", String.valueOf(r.getEmail()));
					else
						unit.put("Email", "");
					if (r.getWorkReferences() != null)
						unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
					else
						unit.put("WorkReferences", "");
					if (r.getCreditReferences() != null)
						unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
					else
						unit.put("CreditReferences", "");
					unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
					unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
					unit.put("CreditScore", String.valueOf(r.getCreditScore()));
					dataLoanRequest.add(unit);
				
				
				tableLoanRequest.setItems(dataLoanRequest);
				operation_return_pane.setContent(tableLoanRequest);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void markRequestValid() {
		
		System.out.println("execute markRequestValid");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: markRequestValid in service: EnterValidatedCreditReferencesModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(entervalidatedcreditreferencesmodule_service.markRequestValid(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listTenLoanRequest() {
		
		System.out.println("execute listTenLoanRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listTenLoanRequest in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
					List<LoanRequest> result = evaluateloanrequestmodule_service.listTenLoanRequest(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
					tableLoanRequest_Status.setMinWidth("Status".length()*10);
					tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Status);
					TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
					tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
					tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
					TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
					tableLoanRequest_Name.setMinWidth("Name".length()*10);
					tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Name);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
					tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
					tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
					tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
					tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
					TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
					tableLoanRequest_Income.setMinWidth("Income".length()*10);
					tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Income);
					TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
					tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
					tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
					tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
					tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
					TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
					tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
					tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
					TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
					tableLoanRequest_Email.setMinWidth("Email".length()*10);
					tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Email);
					TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
					tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
					tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
					tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
					tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
					tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
					tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
					tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
					tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
					tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
					tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
					
					ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
					for (LoanRequest r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Status", String.valueOf(r.getStatus()));
						unit.put("RequestID", String.valueOf(r.getRequestID()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
						if (r.getLoanPurpose() != null)
							unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
						else
							unit.put("LoanPurpose", "");
						unit.put("Income", String.valueOf(r.getIncome()));
						unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
						if (r.getPostalAddress() != null)
							unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
						else
							unit.put("PostalAddress", "");
						unit.put("ZipCode", String.valueOf(r.getZipCode()));
						if (r.getEmail() != null)
							unit.put("Email", String.valueOf(r.getEmail()));
						else
							unit.put("Email", "");
						if (r.getWorkReferences() != null)
							unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
						else
							unit.put("WorkReferences", "");
						if (r.getCreditReferences() != null)
							unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
						else
							unit.put("CreditReferences", "");
						unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
						unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
						unit.put("CreditScore", String.valueOf(r.getCreditScore()));
						dataLoanRequest.add(unit);
					}
					
					tableLoanRequest.setItems(dataLoanRequest);
					operation_return_pane.setContent(tableLoanRequest);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void chooseOneForReview() {
		
		System.out.println("execute chooseOneForReview");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: chooseOneForReview in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
				LoanRequest r = evaluateloanrequestmodule_service.chooseOneForReview(
				Integer.valueOf(chooseOneForReview_requestid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
				tableLoanRequest_Status.setMinWidth("Status".length()*10);
				tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Status);
				TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
				tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
				tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
				TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
				tableLoanRequest_Name.setMinWidth("Name".length()*10);
				tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Name);
				TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
				tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
				tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
				TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
				tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
				tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
				TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
				tableLoanRequest_Income.setMinWidth("Income".length()*10);
				tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Income);
				TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
				tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
				tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
				tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
				tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
				TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
				tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
				tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
				TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
				tableLoanRequest_Email.setMinWidth("Email".length()*10);
				tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_Email);
				TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
				tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
				tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
				TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
				tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
				tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
				TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
				tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
				tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
				tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
				tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
				TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
				tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
				tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
				    }
				});	
				tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
				
				ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Status", String.valueOf(r.getStatus()));
					unit.put("RequestID", String.valueOf(r.getRequestID()));
					if (r.getName() != null)
						unit.put("Name", String.valueOf(r.getName()));
					else
						unit.put("Name", "");
					unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
					if (r.getLoanPurpose() != null)
						unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
					else
						unit.put("LoanPurpose", "");
					unit.put("Income", String.valueOf(r.getIncome()));
					unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
					if (r.getPostalAddress() != null)
						unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
					else
						unit.put("PostalAddress", "");
					unit.put("ZipCode", String.valueOf(r.getZipCode()));
					if (r.getEmail() != null)
						unit.put("Email", String.valueOf(r.getEmail()));
					else
						unit.put("Email", "");
					if (r.getWorkReferences() != null)
						unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
					else
						unit.put("WorkReferences", "");
					if (r.getCreditReferences() != null)
						unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
					else
						unit.put("CreditReferences", "");
					unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
					unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
					unit.put("CreditScore", String.valueOf(r.getCreditScore()));
					dataLoanRequest.add(unit);
				
				
				tableLoanRequest.setItems(dataLoanRequest);
				operation_return_pane.setContent(tableLoanRequest);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void checkCreditHistory() {
		
		System.out.println("execute checkCreditHistory");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: checkCreditHistory in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
				CreditHistory r = evaluateloanrequestmodule_service.checkCreditHistory(
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCreditHistory = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCreditHistory_OutstandingDebt = new TableColumn<Map<String, String>, String>("OutstandingDebt");
				tableCreditHistory_OutstandingDebt.setMinWidth("OutstandingDebt".length()*10);
				tableCreditHistory_OutstandingDebt.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("OutstandingDebt"));
				    }
				});	
				tableCreditHistory.getColumns().add(tableCreditHistory_OutstandingDebt);
				TableColumn<Map<String, String>, String> tableCreditHistory_BadDebits = new TableColumn<Map<String, String>, String>("BadDebits");
				tableCreditHistory_BadDebits.setMinWidth("BadDebits".length()*10);
				tableCreditHistory_BadDebits.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BadDebits"));
				    }
				});	
				tableCreditHistory.getColumns().add(tableCreditHistory_BadDebits);
				
				ObservableList<Map<String, String>> dataCreditHistory = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("OutstandingDebt", String.valueOf(r.getOutstandingDebt()));
					unit.put("BadDebits", String.valueOf(r.getBadDebits()));
					dataCreditHistory.add(unit);
				
				
				tableCreditHistory.setItems(dataCreditHistory);
				operation_return_pane.setContent(tableCreditHistory);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void reviewCheckingAccount() {
		
		System.out.println("execute reviewCheckingAccount");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: reviewCheckingAccount in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
				CheckingAccount r = evaluateloanrequestmodule_service.reviewCheckingAccount(
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCheckingAccount = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCheckingAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
				tableCheckingAccount_Balance.setMinWidth("Balance".length()*10);
				tableCheckingAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
				    }
				});	
				tableCheckingAccount.getColumns().add(tableCheckingAccount_Balance);
				TableColumn<Map<String, String>, String> tableCheckingAccount_Status = new TableColumn<Map<String, String>, String>("Status");
				tableCheckingAccount_Status.setMinWidth("Status".length()*10);
				tableCheckingAccount_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
				    }
				});	
				tableCheckingAccount.getColumns().add(tableCheckingAccount_Status);
				
				ObservableList<Map<String, String>> dataCheckingAccount = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Balance", String.valueOf(r.getBalance()));
					unit.put("Status", String.valueOf(r.getStatus()));
					dataCheckingAccount.add(unit);
				
				
				tableCheckingAccount.setItems(dataCheckingAccount);
				operation_return_pane.setContent(tableCheckingAccount);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listAvaiableLoanTerm() {
		
		System.out.println("execute listAvaiableLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listAvaiableLoanTerm in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
					List<LoanTerm> result = evaluateloanrequestmodule_service.listAvaiableLoanTerm(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableLoanTerm = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableLoanTerm_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
					tableLoanTerm_ItemID.setMinWidth("ItemID".length()*10);
					tableLoanTerm_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
					    }
					});	
					tableLoanTerm.getColumns().add(tableLoanTerm_ItemID);
					TableColumn<Map<String, String>, String> tableLoanTerm_Content = new TableColumn<Map<String, String>, String>("Content");
					tableLoanTerm_Content.setMinWidth("Content".length()*10);
					tableLoanTerm_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
					    }
					});	
					tableLoanTerm.getColumns().add(tableLoanTerm_Content);
					
					ObservableList<Map<String, String>> dataLoanTerm = FXCollections.observableArrayList();
					for (LoanTerm r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("ItemID", String.valueOf(r.getItemID()));
						if (r.getContent() != null)
							unit.put("Content", String.valueOf(r.getContent()));
						else
							unit.put("Content", "");
						dataLoanTerm.add(unit);
					}
					
					tableLoanTerm.setItems(dataLoanTerm);
					operation_return_pane.setContent(tableLoanTerm);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void addLoanTerm() {
		
		System.out.println("execute addLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: addLoanTerm in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(evaluateloanrequestmodule_service.addLoanTerm(
			Integer.valueOf(addLoanTerm_termid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void approveLoanRequest() {
		
		System.out.println("execute approveLoanRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: approveLoanRequest in service: EvaluateLoanRequestModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(evaluateloanrequestmodule_service.approveLoanRequest(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listApprovalRequest() {
		
		System.out.println("execute listApprovalRequest");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listApprovalRequest in service: GenerateLoanLetterAndAgreementModule ");
		
		try {
			//invoke op with parameters
					List<LoanRequest> result = generateloanletterandagreementmodule_service.listApprovalRequest(
					);
				
					//binding result to GUI
					TableView<Map<String, String>> tableLoanRequest = new TableView<Map<String, String>>();
					TableColumn<Map<String, String>, String> tableLoanRequest_Status = new TableColumn<Map<String, String>, String>("Status");
					tableLoanRequest_Status.setMinWidth("Status".length()*10);
					tableLoanRequest_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Status);
					TableColumn<Map<String, String>, String> tableLoanRequest_RequestID = new TableColumn<Map<String, String>, String>("RequestID");
					tableLoanRequest_RequestID.setMinWidth("RequestID".length()*10);
					tableLoanRequest_RequestID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("RequestID"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_RequestID);
					TableColumn<Map<String, String>, String> tableLoanRequest_Name = new TableColumn<Map<String, String>, String>("Name");
					tableLoanRequest_Name.setMinWidth("Name".length()*10);
					tableLoanRequest_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Name);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanAmount = new TableColumn<Map<String, String>, String>("LoanAmount");
					tableLoanRequest_LoanAmount.setMinWidth("LoanAmount".length()*10);
					tableLoanRequest_LoanAmount.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanAmount"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanAmount);
					TableColumn<Map<String, String>, String> tableLoanRequest_LoanPurpose = new TableColumn<Map<String, String>, String>("LoanPurpose");
					tableLoanRequest_LoanPurpose.setMinWidth("LoanPurpose".length()*10);
					tableLoanRequest_LoanPurpose.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("LoanPurpose"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_LoanPurpose);
					TableColumn<Map<String, String>, String> tableLoanRequest_Income = new TableColumn<Map<String, String>, String>("Income");
					tableLoanRequest_Income.setMinWidth("Income".length()*10);
					tableLoanRequest_Income.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Income"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Income);
					TableColumn<Map<String, String>, String> tableLoanRequest_PhoneNumber = new TableColumn<Map<String, String>, String>("PhoneNumber");
					tableLoanRequest_PhoneNumber.setMinWidth("PhoneNumber".length()*10);
					tableLoanRequest_PhoneNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PhoneNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PhoneNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_PostalAddress = new TableColumn<Map<String, String>, String>("PostalAddress");
					tableLoanRequest_PostalAddress.setMinWidth("PostalAddress".length()*10);
					tableLoanRequest_PostalAddress.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("PostalAddress"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_PostalAddress);
					TableColumn<Map<String, String>, String> tableLoanRequest_ZipCode = new TableColumn<Map<String, String>, String>("ZipCode");
					tableLoanRequest_ZipCode.setMinWidth("ZipCode".length()*10);
					tableLoanRequest_ZipCode.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("ZipCode"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_ZipCode);
					TableColumn<Map<String, String>, String> tableLoanRequest_Email = new TableColumn<Map<String, String>, String>("Email");
					tableLoanRequest_Email.setMinWidth("Email".length()*10);
					tableLoanRequest_Email.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("Email"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_Email);
					TableColumn<Map<String, String>, String> tableLoanRequest_WorkReferences = new TableColumn<Map<String, String>, String>("WorkReferences");
					tableLoanRequest_WorkReferences.setMinWidth("WorkReferences".length()*10);
					tableLoanRequest_WorkReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("WorkReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_WorkReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditReferences = new TableColumn<Map<String, String>, String>("CreditReferences");
					tableLoanRequest_CreditReferences.setMinWidth("CreditReferences".length()*10);
					tableLoanRequest_CreditReferences.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditReferences"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditReferences);
					TableColumn<Map<String, String>, String> tableLoanRequest_CheckingAccountNumber = new TableColumn<Map<String, String>, String>("CheckingAccountNumber");
					tableLoanRequest_CheckingAccountNumber.setMinWidth("CheckingAccountNumber".length()*10);
					tableLoanRequest_CheckingAccountNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CheckingAccountNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CheckingAccountNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_SecurityNumber = new TableColumn<Map<String, String>, String>("SecurityNumber");
					tableLoanRequest_SecurityNumber.setMinWidth("SecurityNumber".length()*10);
					tableLoanRequest_SecurityNumber.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("SecurityNumber"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_SecurityNumber);
					TableColumn<Map<String, String>, String> tableLoanRequest_CreditScore = new TableColumn<Map<String, String>, String>("CreditScore");
					tableLoanRequest_CreditScore.setMinWidth("CreditScore".length()*10);
					tableLoanRequest_CreditScore.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
						@Override
					    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
					        return new ReadOnlyStringWrapper(data.getValue().get("CreditScore"));
					    }
					});	
					tableLoanRequest.getColumns().add(tableLoanRequest_CreditScore);
					
					ObservableList<Map<String, String>> dataLoanRequest = FXCollections.observableArrayList();
					for (LoanRequest r : result) {
						Map<String, String> unit = new HashMap<String, String>();
						unit.put("Status", String.valueOf(r.getStatus()));
						unit.put("RequestID", String.valueOf(r.getRequestID()));
						if (r.getName() != null)
							unit.put("Name", String.valueOf(r.getName()));
						else
							unit.put("Name", "");
						unit.put("LoanAmount", String.valueOf(r.getLoanAmount()));
						if (r.getLoanPurpose() != null)
							unit.put("LoanPurpose", String.valueOf(r.getLoanPurpose()));
						else
							unit.put("LoanPurpose", "");
						unit.put("Income", String.valueOf(r.getIncome()));
						unit.put("PhoneNumber", String.valueOf(r.getPhoneNumber()));
						if (r.getPostalAddress() != null)
							unit.put("PostalAddress", String.valueOf(r.getPostalAddress()));
						else
							unit.put("PostalAddress", "");
						unit.put("ZipCode", String.valueOf(r.getZipCode()));
						if (r.getEmail() != null)
							unit.put("Email", String.valueOf(r.getEmail()));
						else
							unit.put("Email", "");
						if (r.getWorkReferences() != null)
							unit.put("WorkReferences", String.valueOf(r.getWorkReferences()));
						else
							unit.put("WorkReferences", "");
						if (r.getCreditReferences() != null)
							unit.put("CreditReferences", String.valueOf(r.getCreditReferences()));
						else
							unit.put("CreditReferences", "");
						unit.put("CheckingAccountNumber", String.valueOf(r.getCheckingAccountNumber()));
						unit.put("SecurityNumber", String.valueOf(r.getSecurityNumber()));
						unit.put("CreditScore", String.valueOf(r.getCreditScore()));
						dataLoanRequest.add(unit);
					}
					
					tableLoanRequest.setItems(dataLoanRequest);
					operation_return_pane.setContent(tableLoanRequest);
				
			
			//return type is entity
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void genereateApprovalLetter() {
		
		System.out.println("execute genereateApprovalLetter");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: genereateApprovalLetter in service: GenerateLoanLetterAndAgreementModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(generateloanletterandagreementmodule_service.genereateApprovalLetter(
			Integer.valueOf(genereateApprovalLetter_id_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void emailToAppliant() {
		
		System.out.println("execute emailToAppliant");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: emailToAppliant in service: GenerateLoanLetterAndAgreementModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(generateloanletterandagreementmodule_service.emailToAppliant(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void generateLoanAgreement() {
		
		System.out.println("execute generateLoanAgreement");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: generateLoanAgreement in service: GenerateLoanLetterAndAgreementModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(generateloanletterandagreementmodule_service.generateLoanAgreement(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void printLoanAgreement() {
		
		System.out.println("execute printLoanAgreement");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: printLoanAgreement in service: GenerateLoanLetterAndAgreementModule ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(generateloanletterandagreementmodule_service.printLoanAgreement(
			Integer.valueOf(printLoanAgreement_number_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void bookNewLoan() {
		
		System.out.println("execute bookNewLoan");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: bookNewLoan in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.bookNewLoan(
			Integer.valueOf(bookNewLoan_requestid_t.getText()),
			Integer.valueOf(bookNewLoan_loanid_t.getText()),
			Integer.valueOf(bookNewLoan_accountid_t.getText()),
			LocalDate.parse(bookNewLoan_startdate_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			LocalDate.parse(bookNewLoan_enddate_t.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))						,
			Integer.valueOf(bookNewLoan_repaymentdays_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void generateStandardPaymentNotice() {
		
		System.out.println("execute generateStandardPaymentNotice");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: generateStandardPaymentNotice in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.generateStandardPaymentNotice(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void generateLateNotice() {
		
		System.out.println("execute generateLateNotice");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: generateLateNotice in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.generateLateNotice(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void loanPayment() {
		
		System.out.println("execute loanPayment");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: loanPayment in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.loanPayment(
			Integer.valueOf(loanPayment_loanid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void closeOutLoan() {
		
		System.out.println("execute closeOutLoan");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: closeOutLoan in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.closeOutLoan(
			Integer.valueOf(closeOutLoan_loanid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createLoanTerm() {
		
		System.out.println("execute createLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createLoanTerm in service: ManageLoanTermCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageloantermcrudservice_service.createLoanTerm(
			Integer.valueOf(createLoanTerm_itemid_t.getText()),
			createLoanTerm_content_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void queryLoanTerm() {
		
		System.out.println("execute queryLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: queryLoanTerm in service: ManageLoanTermCRUDService ");
		
		try {
			//invoke op with parameters
				LoanTerm r = manageloantermcrudservice_service.queryLoanTerm(
				Integer.valueOf(queryLoanTerm_itemid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableLoanTerm = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableLoanTerm_ItemID = new TableColumn<Map<String, String>, String>("ItemID");
				tableLoanTerm_ItemID.setMinWidth("ItemID".length()*10);
				tableLoanTerm_ItemID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("ItemID"));
				    }
				});	
				tableLoanTerm.getColumns().add(tableLoanTerm_ItemID);
				TableColumn<Map<String, String>, String> tableLoanTerm_Content = new TableColumn<Map<String, String>, String>("Content");
				tableLoanTerm_Content.setMinWidth("Content".length()*10);
				tableLoanTerm_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
				    }
				});	
				tableLoanTerm.getColumns().add(tableLoanTerm_Content);
				
				ObservableList<Map<String, String>> dataLoanTerm = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("ItemID", String.valueOf(r.getItemID()));
					if (r.getContent() != null)
						unit.put("Content", String.valueOf(r.getContent()));
					else
						unit.put("Content", "");
					dataLoanTerm.add(unit);
				
				
				tableLoanTerm.setItems(dataLoanTerm);
				operation_return_pane.setContent(tableLoanTerm);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void modifyLoanTerm() {
		
		System.out.println("execute modifyLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: modifyLoanTerm in service: ManageLoanTermCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageloantermcrudservice_service.modifyLoanTerm(
			Integer.valueOf(modifyLoanTerm_itemid_t.getText()),
			modifyLoanTerm_content_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void deleteLoanTerm() {
		
		System.out.println("execute deleteLoanTerm");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: deleteLoanTerm in service: ManageLoanTermCRUDService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(manageloantermcrudservice_service.deleteLoanTerm(
			Integer.valueOf(deleteLoanTerm_itemid_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getCheckingAccountStatus() {
		
		System.out.println("execute getCheckingAccountStatus");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getCheckingAccountStatus in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
				CheckingAccount r = thirdpartyservices_service.getCheckingAccountStatus(
				Integer.valueOf(getCheckingAccountStatus_cid_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCheckingAccount = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCheckingAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
				tableCheckingAccount_Balance.setMinWidth("Balance".length()*10);
				tableCheckingAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
				    }
				});	
				tableCheckingAccount.getColumns().add(tableCheckingAccount_Balance);
				TableColumn<Map<String, String>, String> tableCheckingAccount_Status = new TableColumn<Map<String, String>, String>("Status");
				tableCheckingAccount_Status.setMinWidth("Status".length()*10);
				tableCheckingAccount_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
				    }
				});	
				tableCheckingAccount.getColumns().add(tableCheckingAccount_Status);
				
				ObservableList<Map<String, String>> dataCheckingAccount = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("Balance", String.valueOf(r.getBalance()));
					unit.put("Status", String.valueOf(r.getStatus()));
					dataCheckingAccount.add(unit);
				
				
				tableCheckingAccount.setItems(dataCheckingAccount);
				operation_return_pane.setContent(tableCheckingAccount);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void getCreditHistory() {
		
		System.out.println("execute getCreditHistory");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: getCreditHistory in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
				CreditHistory r = thirdpartyservices_service.getCreditHistory(
				Integer.valueOf(getCreditHistory_securityid_t.getText()),
				getCreditHistory_name_t.getText()
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableCreditHistory = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableCreditHistory_OutstandingDebt = new TableColumn<Map<String, String>, String>("OutstandingDebt");
				tableCreditHistory_OutstandingDebt.setMinWidth("OutstandingDebt".length()*10);
				tableCreditHistory_OutstandingDebt.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("OutstandingDebt"));
				    }
				});	
				tableCreditHistory.getColumns().add(tableCreditHistory_OutstandingDebt);
				TableColumn<Map<String, String>, String> tableCreditHistory_BadDebits = new TableColumn<Map<String, String>, String>("BadDebits");
				tableCreditHistory_BadDebits.setMinWidth("BadDebits".length()*10);
				tableCreditHistory_BadDebits.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("BadDebits"));
				    }
				});	
				tableCreditHistory.getColumns().add(tableCreditHistory_BadDebits);
				
				ObservableList<Map<String, String>> dataCreditHistory = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("OutstandingDebt", String.valueOf(r.getOutstandingDebt()));
					unit.put("BadDebits", String.valueOf(r.getBadDebits()));
					dataCreditHistory.add(unit);
				
				
				tableCreditHistory.setItems(dataCreditHistory);
				operation_return_pane.setContent(tableCreditHistory);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void sendEmail() {
		
		System.out.println("execute sendEmail");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: sendEmail in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.sendEmail(
			sendEmail_emailaddress_t.getText(),
			sendEmail_title_t.getText(),
			sendEmail_content_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void print() {
		
		System.out.println("execute print");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: print in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.print(
			print_content_t.getText(),
			Integer.valueOf(print_numbers_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void createLoanAccount() {
		
		System.out.println("execute createLoanAccount");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: createLoanAccount in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
				LoanAccount r = thirdpartyservices_service.createLoanAccount(
				Integer.valueOf(createLoanAccount_id_t.getText())
				);
			
				//binding result to GUI
				TableView<Map<String, String>> tableLoanAccount = new TableView<Map<String, String>>();
				TableColumn<Map<String, String>, String> tableLoanAccount_LoanAccountID = new TableColumn<Map<String, String>, String>("LoanAccountID");
				tableLoanAccount_LoanAccountID.setMinWidth("LoanAccountID".length()*10);
				tableLoanAccount_LoanAccountID.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("LoanAccountID"));
				    }
				});	
				tableLoanAccount.getColumns().add(tableLoanAccount_LoanAccountID);
				TableColumn<Map<String, String>, String> tableLoanAccount_Balance = new TableColumn<Map<String, String>, String>("Balance");
				tableLoanAccount_Balance.setMinWidth("Balance".length()*10);
				tableLoanAccount_Balance.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Balance"));
				    }
				});	
				tableLoanAccount.getColumns().add(tableLoanAccount_Balance);
				TableColumn<Map<String, String>, String> tableLoanAccount_Status = new TableColumn<Map<String, String>, String>("Status");
				tableLoanAccount_Status.setMinWidth("Status".length()*10);
				tableLoanAccount_Status.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
					@Override
				    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
				        return new ReadOnlyStringWrapper(data.getValue().get("Status"));
				    }
				});	
				tableLoanAccount.getColumns().add(tableLoanAccount_Status);
				
				ObservableList<Map<String, String>> dataLoanAccount = FXCollections.observableArrayList();
				
					Map<String, String> unit = new HashMap<String, String>();
					unit.put("LoanAccountID", String.valueOf(r.getLoanAccountID()));
					unit.put("Balance", String.valueOf(r.getBalance()));
					unit.put("Status", String.valueOf(r.getStatus()));
					dataLoanAccount.add(unit);
				
				
				tableLoanAccount.setItems(dataLoanAccount);
				operation_return_pane.setContent(tableLoanAccount);					
					
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void transferFunds() {
		
		System.out.println("execute transferFunds");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: transferFunds in service: ThirdPartyServices ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(thirdpartyservices_service.transferFunds(
			Integer.valueOf(transferFunds_id_t.getText()),
			Float.valueOf(transferFunds_amount_t.getText())
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void listBookedLoans() {
		
		System.out.println("execute listBookedLoans");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: listBookedLoans in service: LoanProcessingSystemSystem ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(loanprocessingsystemsystem_service.listBookedLoans(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_loanofficer;
	@FXML
	TreeView<String> actor_treeview_loanassistant;
	@FXML
	TreeView<String> actor_treeview_loanclerk;
	@FXML
	TreeView<String> actor_treeview_scheduler;
	@FXML
	TreeView<String> actor_treeview_applicant;

	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	SubmitLoanRequestModule submitloanrequestmodule_service;
	ThirdPartyServices thirdpartyservices_service;
	EnterValidatedCreditReferencesModule entervalidatedcreditreferencesmodule_service;
	EvaluateLoanRequestModule evaluateloanrequestmodule_service;
	GenerateLoanLetterAndAgreementModule generateloanletterandagreementmodule_service;
	LoanProcessingSystemSystem loanprocessingsystemsystem_service;
	ManageLoanTermCRUDService manageloantermcrudservice_service;
	
	ClassInfo loanrequest;
	ClassInfo loan;
	ClassInfo loanterm;
	ClassInfo checkingaccount;
	ClassInfo credithistory;
	ClassInfo loanaccount;
	ClassInfo approvalletter;
	ClassInfo loanagreement;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField enterLoanInformation_requestid_t;
	TextField enterLoanInformation_name_t;
	TextField enterLoanInformation_loanamount_t;
	TextField enterLoanInformation_loanpurpose_t;
	TextField enterLoanInformation_income_t;
	TextField enterLoanInformation_phonenumber_t;
	TextField enterLoanInformation_postaladdress_t;
	TextField enterLoanInformation_zipcode_t;
	TextField enterLoanInformation_email_t;
	TextField enterLoanInformation_workreferences_t;
	TextField enterLoanInformation_creditreferences_t;
	TextField enterLoanInformation_checkingaccountnumber_t;
	TextField enterLoanInformation_securitynumber_t;
	TextField chooseLoanRequest_requestid_t;
	TextField chooseOneForReview_requestid_t;
	TextField addLoanTerm_termid_t;
	TextField genereateApprovalLetter_id_t;
	TextField printLoanAgreement_number_t;
	TextField bookNewLoan_requestid_t;
	TextField bookNewLoan_loanid_t;
	TextField bookNewLoan_accountid_t;
	TextField bookNewLoan_startdate_t;
	TextField bookNewLoan_enddate_t;
	TextField bookNewLoan_repaymentdays_t;
	TextField loanPayment_loanid_t;
	TextField closeOutLoan_loanid_t;
	TextField createLoanTerm_itemid_t;
	TextField createLoanTerm_content_t;
	TextField queryLoanTerm_itemid_t;
	TextField modifyLoanTerm_itemid_t;
	TextField modifyLoanTerm_content_t;
	TextField deleteLoanTerm_itemid_t;
	TextField getCheckingAccountStatus_cid_t;
	TextField getCreditHistory_securityid_t;
	TextField getCreditHistory_name_t;
	TextField sendEmail_emailaddress_t;
	TextField sendEmail_title_t;
	TextField sendEmail_content_t;
	TextField print_content_t;
	TextField print_numbers_t;
	TextField createLoanAccount_id_t;
	TextField transferFunds_id_t;
	TextField transferFunds_amount_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
