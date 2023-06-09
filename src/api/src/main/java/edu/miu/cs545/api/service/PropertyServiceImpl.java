
package edu.miu.cs545.api.service;

import edu.miu.cs545.api.dto.PropertyDto;
import edu.miu.cs545.api.entity.Property;
import edu.miu.cs545.api.entity.User;
import edu.miu.cs545.api.entity.Owner;
import edu.miu.cs545.api.entity.PropertyState;
import edu.miu.cs545.api.repository.AddressRepository;
import edu.miu.cs545.api.repository.PropertyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    EntityManager em;
    @Autowired
    PropertyRepository propertyRepo;

    @Autowired
    AddressRepository addressRepo;

    @Autowired
    ModelMapper modelMapper;

    public PropertyDto createProperty(PropertyDto property, User user) {
        var address = addressRepo.save(modelMapper.map(property, Property.class).getAddress());
        Property p = modelMapper.map(property, Property.class);
        p.setAddress(address);
        p.setOwner((Owner)user.getPerson()); // get the Owner info here
        p.setStatus(PropertyState.AVAILABLE);
        return modelMapper.map(propertyRepo.save(p), PropertyDto.class) ;
    }

    public PropertyDto findPropertyById(Long id) {
        Property property = propertyRepo.findById(id).orElseGet(null);
        return modelMapper.map(property, PropertyDto.class);
    }

    public PropertyDto updateProperty(PropertyDto property) {
        Property p = modelMapper.map(property, Property.class);
        return modelMapper.map(propertyRepo.save(p), PropertyDto.class) ;

    }

    public void deleteProperty(long id, User user) throws Exception{
        Property property = propertyRepo.findById(id).orElseThrow();
        if(property.getOwner().getId() != user.getPerson().getId() || property.getStatus() != PropertyState.AVAILABLE){
            throw new Exception("Only owner can delete the properties with status 'AVAILABLE'");
        }
        propertyRepo.delete(property);
    }

    public List<PropertyDto> findAll() {
        return propertyRepo.findByOrderByIdDesc().stream().map(x-> modelMapper.map(x, PropertyDto.class)).toList();
    }

    @Override
    public List<PropertyDto> findProperByFilter(String city, Double max, Double min, Integer room) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Property> cq = cb.createQuery(Property.class);

        Root<Property> property = cq.from(Property.class);
        List<Predicate> predicates = new ArrayList<>();

        if (city != null && city != "") {
            predicates.add(cb.like(property.get("address").get("city"), "%" + city + "%"));
        }
        if (max != null && max != 0) {
            predicates.add(cb.lessThanOrEqualTo(property.get("price"), max));
        }
        if (min != null && min != 0) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("price"), min));
        }
        if (room != null && room != 0) {
            predicates.add(cb.greaterThanOrEqualTo(property.get("noOfBedrooms"), room));
        }
        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList().stream().map(x->modelMapper.map(x, PropertyDto.class)).toList();
    }

}
