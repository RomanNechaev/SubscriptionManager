package ru.matmex.subscription.services.impl.export.reports;

import ru.matmex.subscription.models.user.UserModel;

import java.util.Map;

public enum Report {
    AveragePriceCategory {
        @Override
        public Map<String, Double> calculate(UserModel userModel) {
            return new CalculateAveragePriceCategory().apply(userModel);
        }
    },
    TotalPriceCategory {
        public Map<String, Double> calculate(UserModel userModel) {
            return new CalculateTotalPriceCategory().apply(userModel);
        }
    };

    public abstract Map<String, Double> calculate(UserModel userModel);
}
