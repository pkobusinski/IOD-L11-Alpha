package pl.put.poznan.buildingInfo.logic.locations;

import pl.put.poznan.buildingInfo.logic.visitors.Visitor;

public interface Visitable {
    double accept(Visitor visitor);
}
