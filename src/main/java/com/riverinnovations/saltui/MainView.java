package com.riverinnovations.saltui;

import com.riverinnovations.saltui.model.DuplicateNameException;
import com.riverinnovations.saltui.model.user.User;
import com.riverinnovations.saltui.model.user.Users;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.text.NumberFormat;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    private static final String NULL_REP_EMPTY_STRING = "";

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

    /**
     * Utility method to add columns to the grid.
     * Seprated out so that warnings can be suppressed for nullable return value from
     * user bean into null-allowed value in number renderer.
     * @param grid The grid to add columns to.
     */
    @SuppressWarnings("methodref.return.invalid")
    private void addColumns(Grid<User> grid) {
        grid.addColumn(User::getName).setHeader("User Name");
        grid.addColumn(user -> user.getGecosFullname() == null ? "" : user.getGecosFullname()).setHeader("Full Name");
        grid.addColumn(new NumberRenderer<>(User::getUid,
                NumberFormat.getIntegerInstance(),
                NULL_REP_EMPTY_STRING))
                .setHeader("UID");
        grid.addColumn(new NumberRenderer<>(User::getGid,
                NumberFormat.getIntegerInstance(),
                NULL_REP_EMPTY_STRING))
                .setHeader("GID");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

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
        this.addColumns(grid);
        add(grid);
        setHeight("100vh");

        // Selection listener
        grid.addSelectionListener(event -> {
            if (event.getFirstSelectedItem().isPresent()) {
                User selected = event.getFirstSelectedItem().get();
                Notification.show("Selected " + selected.getName());
            }
        });
    }
}

