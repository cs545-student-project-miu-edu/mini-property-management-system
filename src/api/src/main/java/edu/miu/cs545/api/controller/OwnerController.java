package edu.miu.cs545.api.controller;

import edu.miu.cs545.api.dto.OwnerDto;
import edu.miu.cs545.api.dto.OfferDto;
import edu.miu.cs545.api.entity.Owner;
import edu.miu.cs545.api.entity.User;
import edu.miu.cs545.api.service.OwnerService;
import edu.miu.cs545.api.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owners")
public class OwnerController {

    @Autowired
    OwnerService ownerService;
    @Autowired
    private OfferService offerService;    
    @Autowired
    ControllerSecurityUtil controllerSecurityUtil;
    @GetMapping
    public ResponseEntity<List<OwnerDto>> getAll(){
        return ResponseEntity.ok(ownerService.getAll());
    }
    @GetMapping("/{id}/offers")
    public ResponseEntity<List<OfferDto>> checkOfferHistory(@PathVariable long id) {
        //Ignore ID and send only the allowed offers
        User user = controllerSecurityUtil.getLoggedinUser();
        List<OfferDto> offers = ownerService.findOffersByOwnerId(user.getPerson().getId());
        System.out.println("contrller checkOfferHistory size: " + offers.size());
        return ResponseEntity.ok(offers);
    }
    @PostMapping("/register")
    ResponseEntity<Long> register(@RequestBody OwnerDto ownerDto){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body (ownerService.register(ownerDto));
    }

    @PutMapping("/approve/{id}")
    ResponseEntity<Boolean> approveOwner(@RequestBody OwnerDto ownerDto, @PathVariable long id) {
        Owner owner = ownerService.findById(id);
        owner.setApproved(ownerDto.getApproved());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body (ownerService.save(owner));
    }

    @PutMapping("/black-list/{id}")
    ResponseEntity<Boolean> blackListOwner(@RequestBody OwnerDto ownerDto, @PathVariable long id) {
        Owner owner = ownerService.findById(id);
        owner.setBlackListed(ownerDto.getBlackListed());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body (ownerService.save(owner));
    }

    @PostMapping("/{ownerId}/offer/{offerId}/evaluate")
    public ResponseEntity<Boolean> startEvaluating(@PathVariable Long ownerId, @PathVariable Long offerId) throws Exception {
        //Ignore owner Id
        User user = controllerSecurityUtil.getLoggedinUser();
        offerService.startEvaluating(user.getPerson().getId(), offerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{ownerId}/offer/{offerId}/accept")
    public ResponseEntity<Boolean> accept(@PathVariable Long ownerId, @PathVariable Long offerId) throws Exception {
        //Ignore owner Id
        User user = controllerSecurityUtil.getLoggedinUser();
        offerService.accept(user.getPerson().getId(), offerId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{ownerId}/offer/{offerId}/cancel")
    public ResponseEntity<Boolean> cancel(@PathVariable Long ownerId, @PathVariable Long offerId) throws Exception {
        //Ignore owner Id
        User user = controllerSecurityUtil.getLoggedinUser();
        offerService.cancelByOwner(user.getPerson().getId(), offerId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{ownerId}/offer/{offerId}/close")
    public ResponseEntity<Boolean> close(@PathVariable Long ownerId, @PathVariable Long offerId) throws Exception {
        //Ignore owner Id
        User user = controllerSecurityUtil.getLoggedinUser();
        offerService.closeByOwner(user.getPerson().getId(), offerId);
        return ResponseEntity.ok().build();
    }
}
