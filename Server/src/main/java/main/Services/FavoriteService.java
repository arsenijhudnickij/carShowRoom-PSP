package main.Services;

import main.DAO.FavoriteDAO;
import main.Interfaces.Service;
import main.Models.Entities.Favorite;
import main.exception.ShowException;
import org.hibernate.HibernateError;

import java.util.List;

public class FavoriteService implements Service {
    FavoriteDAO favoriteDAO = new FavoriteDAO();
    @Override
    public Object findEntity(int id) {
        Favorite favorite = null;
        try {
            favorite = favoriteDAO.findById(id);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return favorite;
    }

    @Override
    public void saveEntity(Object entity) {
        try {
            favoriteDAO.save((Favorite) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void deleteEntity(Object entity) {
        try {
            favoriteDAO.delete((Favorite) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public void updateEntity(Object entity) {
        try {
            favoriteDAO.update((Favorite) entity);
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
    }

    @Override
    public List<Favorite> findAllEntities() {
        List<Favorite> favorites = null;
        try {
            favorites = favoriteDAO.findAll();
        } catch (HibernateError e) {
            ShowException.showNotice(e);
        }
        return favorites;
    }
}
