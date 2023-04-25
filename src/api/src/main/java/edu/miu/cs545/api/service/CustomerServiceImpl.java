package edu.miu.cs545.api.service;

import edu.miu.cs545.api.dto.AddressDto;
import edu.miu.cs545.api.dto.CustomerDto;
import edu.miu.cs545.api.dto.OfferDto;
import edu.miu.cs545.api.dto.UserDto;
import edu.miu.cs545.api.entity.Address;
import edu.miu.cs545.api.entity.Customer;
import edu.miu.cs545.api.entity.User;
import edu.miu.cs545.api.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CustomerDto> findAll() {
        var customersDtoList = new ArrayList<CustomerDto>();
        customerRepo.findAll().forEach(cust -> {
            customersDtoList.add(mapCustomerToDto(cust));
        });
        return customersDtoList;
    }

    @Override
    public CustomerDto findById(long id) {
        return mapCustomerToDto(customerRepo.findById(id).orElse(null));
    }

    @Override
    public boolean deleteById(long id) {
        customerRepo.deleteById(id);
        return true;
    }

    @Override
    public boolean save(CustomerDto customerDto ) {
        customerRepo.save(mapDtoToCustomer(customerDto));
        return true;
    }

    @Override
    public List<OfferDto> findOffersByCustomerId(long customerId) {
        return null;
    }

    private CustomerDto mapCustomerToDto(Customer cust) {
        var customerDto = modelMapper.map(cust, CustomerDto.class);
        customerDto.setAddress(modelMapper.map(cust.getAddress(), AddressDto.class));
        customerDto.setUser(modelMapper.map(cust.getUser(), UserDto.class));
        return  customerDto;
    }

    private Customer mapDtoToCustomer(CustomerDto dto) {
        var customer = modelMapper.map(dto, Customer.class);
        customer.setAddress(modelMapper.map(dto.getAddress(), Address.class));
        customer.setUser(modelMapper.map(dto.getUser(), User.class));
        return customer;
    }
}
