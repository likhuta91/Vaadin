package com.by.lik.hotel;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;


@SuppressWarnings("serial")
@Theme("mytheme")
public class HotelUI extends UI {
	
	final VerticalLayout layout = new VerticalLayout();
	final HotelService hotelService = HotelService.getInstance();
	final Grid<Hotel> hotelGrid = new Grid<>(Hotel.class);
	final TextField nameFilter = new TextField("Enter name");
	final TextField addressFilter = new TextField("Enter address");
	final Button addHotel = new Button("Add hotel");
	final Button deleteHotel = new Button("Delete hotel");
	final HotelEditForm form = new HotelEditForm(this);

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		
		hotelGrid.removeColumn("url");
		hotelGrid.addComponentColumn(e -> new Link(e.getUrl(), new ExternalResource(e.getUrl()))).setCaption("Url");

		setContent(layout);

		HorizontalLayout controls = new HorizontalLayout();
		
		controls.addComponents(nameFilter, addressFilter, addHotel, deleteHotel);
		deleteHotel.setEnabled(false); 
		
		HorizontalLayout content = new HorizontalLayout();
		
		layout.addComponents(controls,  content);
		
		hotelGrid.setColumnOrder("name", "address", "rating", "category");
		
		content.addComponents(hotelGrid, form);
		form.setVisible(false);		
		//hotelGrid.setWidth(100, Unit.PERCENTAGE);
		
		hotelGrid.asSingleSelect().addValueChangeListener(e-> {
			if(e.getValue()!=null) {
				deleteHotel.setEnabled(true);
				form.setHotel(e.getValue());
			}
		});
		
		deleteHotel.addClickListener(e-> {
			Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
			hotelService.delete(delCandidate);
			deleteHotel.setEnabled(false);
			updateList();
			form.setVisible(false);
		});

		nameFilter.addValueChangeListener(e -> updateList());
		nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
		updateList();
		
		addressFilter.addValueChangeListener(e -> updateList());
		addressFilter.setValueChangeMode(ValueChangeMode.LAZY);
		updateList();
		
		addHotel.addClickListener(e -> form.setHotel(new Hotel()));

	}

	public void updateList() {
		List<Hotel> hotelList = hotelService.findAll(nameFilter.getValue(), addressFilter.getValue());
		hotelGrid.setItems(hotelList);
	}

	@WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
	public static class HotelUIServlet extends VaadinServlet {

	}
}
