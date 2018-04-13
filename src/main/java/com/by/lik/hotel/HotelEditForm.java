package com.by.lik.hotel;

import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class HotelEditForm extends FormLayout {
	
	private HotelUI ui;
	private HotelService hotelService= HotelService.getInstance();
	private Hotel hotel;
	private Binder<Hotel> binder = new Binder<>(Hotel.class);
	
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private DateField operatesFrom = new DateField("Date");
	private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");	
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	
	private Button save = new Button ("Save");
	private Button close = new Button("Close");
	
	public HotelEditForm(HotelUI hotelUI) {
		this.ui = hotelUI;
		
		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponents(save, close);
		
		addComponents(name, address, rating, operatesFrom, category, url, description, buttons);
		binder.bindInstanceFields(this);
		
		category.setItems(HotelCategory.values());
		
		save.addClickListener(e ->save());
		close.addClickListener(e ->setVisible(false));
	}
	
	private void save() {
		hotelService.save(hotel);
		ui.updateList();
		setVisible(false);
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.setBean(this.hotel);
		setVisible(true);
		
	}

}
