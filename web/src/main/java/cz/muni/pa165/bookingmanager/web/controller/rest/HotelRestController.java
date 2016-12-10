package cz.muni.pa165.bookingmanager.web.controller.rest;

import cz.muni.pa165.bookingmanager.iface.dto.HotelDto;
import cz.muni.pa165.bookingmanager.iface.facade.HotelFacade;
import cz.muni.pa165.bookingmanager.iface.util.PageInfo;
import cz.muni.pa165.bookingmanager.web.controller.rest.exceptions.ResourceNotCreated;
import cz.muni.pa165.bookingmanager.web.controller.rest.exceptions.ResourceNotFoundException;
import cz.muni.pa165.bookingmanager.web.controller.rest.exceptions.ResourceNotModifiedException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Created by gasior on 10.12.2016.
 */

/**
 * Hotel rest controller can be used to do operations over hotels
 */
@RestController
@RequestMapping("/rest/hotel/")
public class HotelRestController {

    @Inject
    private HotelFacade hotelFacade;

    /**
     * Lists hotels using pages
     * @param pageNumber number of desired page
     * @param pageSize   size of desired page
     * @return One page of hotels
     */
    @RequestMapping(value = "list/{page_number}/{page_size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<HotelDto> listHotels(@PathVariable("page_number") int pageNumber, @PathVariable("page_size") int pageSize) {
        try {
            return hotelFacade.findAll(new PageInfo(pageNumber, pageSize)).getEntries();
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Lists all hotels in defined city
     * @param city hotels that are in city will be listed
     * @return all hotels in city
     */
    @RequestMapping(value = "by_city/{city}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final List<HotelDto> findHotelsByCity(@PathVariable("city") String city) {
        try {
            return hotelFacade.findByCity(city, new PageInfo(0, Integer.MAX_VALUE)).getEntries();
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Returns specific hotel by its id
     * @param id unique identifies of hotel
     * @return one hotel if correct id
     */
    @RequestMapping(value = "by_id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HotelDto findHotel(@PathVariable("id") long id) {
        try {
            Optional<HotelDto> hotel = hotelFacade.findById(id);
            if (hotel.isPresent()) {
                return hotel.get();
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Returns specific hotel by its name
     * @param name unique name identities hotel
     * @return one hotel if name exists
     */
    @RequestMapping(value = "by_name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public final HotelDto findHotelByName(@PathVariable("name") String name) {
        try {
            Optional<HotelDto> hotel = hotelFacade.findByName(name);
            if (hotel.isPresent()) {
                return hotel.get();
            } else {
                throw new ResourceNotFoundException();
            }
        } catch (Exception ex) {
            throw new ResourceNotFoundException();
        }
    }

    /**
     * Updates hotel that already exists in database
     * @param id unique identifier of hotel
     * @param updatedHotel updated version of hotel
     * @return updated version of hotel
     */
    @RequestMapping(value = "update/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final HotelDto updateHotel(@PathVariable("id") long id, @RequestBody HotelDto updatedHotel) {
        if (updatedHotel.getId() != id) {
            throw new ResourceNotModifiedException();
        }
        try {
            return hotelFacade.updateHotelInformation(updatedHotel);
        } catch (Exception ex) {
            throw new ResourceNotModifiedException();
        }
    }

    /**
     * Created hotel
     * @param hotelDto hotel to be created
     * @return created hotel
     */
    @RequestMapping(value = "create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public final HotelDto CreateHotel(@RequestBody HotelDto hotelDto) {
        try {
            return hotelFacade.registerHotel(hotelDto);
        } catch (Exception ex) {
            throw new ResourceNotCreated();
        }
    }
}
