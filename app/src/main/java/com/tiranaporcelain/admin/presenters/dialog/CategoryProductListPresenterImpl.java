package com.tiranaporcelain.admin.presenters.dialog;

import com.tiranaporcelain.admin.interfaces.dialog.CategoryProductListView;
import com.tiranaporcelain.admin.models.db.Category;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.ProductPrice;
import com.tiranaporcelain.admin.models.db.ProductPriceDao;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.List;


/**
 * Created by mphj on 10/24/2017.
 */

public class CategoryProductListPresenterImpl implements CategoryProductListPresenter {

    CategoryProductListView view;

    public CategoryProductListPresenterImpl(CategoryProductListView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList(Category category) {
        ProductPriceDao productPriceDao = DaoManager.session().getProductPriceDao();
        ProductDao productDao = DaoManager.session().getProductDao();
        List<Product> products = productDao.queryBuilder()
                .where(ProductDao.Properties.CategoryId.eq(category.getId()))
                .list();
        for (Product product : products) {
            ProductPrice productPrice = productPriceDao.queryBuilder()
                    .where(ProductPriceDao.Properties.ProductId.eq(product.getId()))
                    .orderDesc(ProductPriceDao.Properties.CreatedAt)
                    .list()
                    .get(0);
            product.setCurrentProductPrice(productPrice);
        }
        view.setAdapter(products);
    }
}
