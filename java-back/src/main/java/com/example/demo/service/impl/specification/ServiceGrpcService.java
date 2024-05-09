package com.example.demo.service.impl.specification;

import com.example.demo.controller.exсeptions.ServiceNotFoundException;
import com.example.demo.mapper.ServicesMapper;
import com.example.demo.model.Services;
import com.example.demo.model.dto.ServicesDto;
import com.example.demo.model.filter.ServiceFilter;
import com.example.demo.repository.ServicesRepository;
import com.example.demo.service.ServicesService;
import com.example.demo.validator.ServicesValidator;
import io.grpc.demo.service.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.google.protobuf.Empty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@GrpcService
@RequiredArgsConstructor
public class ServiceGrpcService extends ServicesServiceGrpc.ServicesServiceImplBase {
    private final ServicesService service;
    private final ServicesMapper mapper;
    private final ServicesRepository repository;
    private final ServicesSpecification specification;
    private final ServicesValidator validator;

    public void findService(FindServiceRequest request,
                           StreamObserver <FindServiceResponse> observer) {
        ServiceFilter filter = mapper.toFilter(request);
        Pageable pageable = mapper.toPageble(request);
        Page<Services> result = repository.findAll(specification.byFilter(filter), pageable);
        FindServiceResponse response = mapper.toResponse(result);
        observer.onNext(response);
        observer.onCompleted();
    }

    public void getService(GetServiceRequest request,
                           StreamObserver <Service> observer) {
        ServicesDto dto = service.findById(request.getId());
        observer.onNext(mapper.toGrpc(dto));
        observer.onCompleted();
    }
    public void updateService(UpdateServiceRequest request,
                              StreamObserver <Empty> observer) {
        Services target = getById((long) request.getId());
        repository.save(mapper.merge(target, mapper.toDto(request.getService())));
        observer.onNext(Empty.getDefaultInstance());
        observer.onCompleted();
    }

    public void createService(CreateServiceRequest request, StreamObserver <Empty> observer) {
        ServicesDto newService = mapper.toDto(request.getService());
        validator.validateService(newService);
        mapper.modelToDto(
                repository.save(
                        mapper.dtoToModel(newService)
                )
        );
        observer.onNext(Empty.getDefaultInstance());
        observer.onCompleted();
    }

    public void deleteService(DeleteServiceRequest request,
                              StreamObserver <DeleteServiceResponse> observer) {
        var service = getById((long) request.getId());
        repository.delete(service);
    }

    private Services getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ServiceNotFoundException(
                String.format("Service with id: %s not found", id))
        );
    }
}
