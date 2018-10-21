package com.riverinnovations.saltui;

import com.riverinnovations.saltui.model.DuplicateNameException;
import com.riverinnovations.saltui.model.user.User;
import com.riverinnovations.saltui.model.user.Users;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        super();
    }

    /**
     * Converts an Integer to a visible string.
     * @param i Integer to convert. Can be null.
     * @return A string representation of the integer. Not localised.
     */
    private String toString(@Nullable Integer i) {
        if (i == null) {
            return  "";
        }
        else {
            return i.toString();
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Button button = new Button("Click me");
        button.addClickListener(event -> Notification.show("Clicked!"));
        add(button);

        // Create some users
        Users users = new Users();
        try {
            User uFoo = new User("Foo");
            uFoo.setUid(1000);
            uFoo.setGid(2000);
            uFoo.setGecosFullname("Mr F Ooooo");
            users.addUser(uFoo);
            users.addUser(new User("Bar"));
            users.addUser(new User("Baz"));
        }
        catch (DuplicateNameException e) {
            Notification.show("Error adding user: " + e.getMessage());
        }

        // Add users to a grid
        Grid<User> grid = new Grid<>();
        grid.setSizeFull();
        grid.setItems(users.getUsers());
        grid.addColumn(User::getName).setHeader("User Name");
        grid.addColumn(user -> user.getGecosFullname() == null ? "" : user.getGecosFullname()).setHeader("Full Name");
        grid.addColumn(user -> this.toString(user.getUid())).setHeader("UID");
        grid.addColumn(user -> this.toString(user.getGid())).setHeader("GID");
        add(grid);
        setHeight("100vh");
    }
}

