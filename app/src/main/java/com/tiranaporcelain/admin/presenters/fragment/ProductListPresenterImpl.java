package com.tiranaporcelain.admin.presenters.fragment;

import com.tiranaporcelain.admin.interfaces.fragment.ProductListView;
import com.tiranaporcelain.admin.models.db.Product;
import com.tiranaporcelain.admin.models.db.ProductDao;
import com.tiranaporcelain.admin.models.db.ProductPrice;
import com.tiranaporcelain.admin.models.db.ProductPriceDao;
import com.tiranaporcelain.admin.utils.DaoManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mphj on 10/22/2017.
 */

public class ProductListPresenterImpl implements ProductListPresenter {

    ProductListView view;

    public ProductListPresenterImpl(ProductListView view){
        this.view = view;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadList() {
        List<Product> list = new ArrayList<>();
        ProductDao productDao = DaoManager.session().getProductDao();
        ProductPriceDao productPriceDao = DaoManager.session().getProductPriceDao();
        List<Product> realmResults = productDao.loadAll();
        for (Product product : realmResults){
            ProductPrice productPrice = productPriceDao.queryBuilder()
                    .where(ProductPriceDao.Properties.ProductId.eq(product.getId()))
                    .orderDesc(ProductPriceDao.Properties.CreatedAt)
                    .unique();
            product.setCurrentProductPrice(productPrice);
            list.add(product);
        }
        view.setAdapter(list);
    }
}
