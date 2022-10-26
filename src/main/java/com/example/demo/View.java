package com.example.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.data.binder.Binder;


@Route("")
public class View extends VerticalLayout {

    private perRepo repo;
    private TextField first_name = new TextField("First Name");
    private TextField last_name = new TextField("Last Name");
    private EmailField email = new EmailField("Email");
    private Grid<People> grd = new Grid<>(People.class);
    private Binder<People> bind = new Binder<>(People.class);

    public View(perRepo repo){
        this.repo = repo;

        grd.setColumns("first_name", "last_name", "email");

        add(getForm(), grd);
        refreshGrid();
    }

    private Component getForm(){

        var layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);

        var addButton = new Button("Add");
        addButton.addClickShortcut(Key.ENTER);
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layout.add(first_name,last_name,email, addButton);

        bind.bindInstanceFields(this);
        addButton.addClickListener(click -> {
                try{
                    var people = new People();
                    bind.writeBean(people);
                    repo.save(people);
                    bind.readBean(new People());
                    refreshGrid();
                }catch (ValidationException e){
                    //
                }
            });

        return layout;
    }

    private void refreshGrid() {
        grd.setItems(repo.findAll());
    }
}
