package br.com.meli.anuncios.services.impl;

import br.com.meli.anuncios.dto.AnuncioDto;
import br.com.meli.anuncios.entitites.Anuncio;
import br.com.meli.anuncios.exceptions.*;
import br.com.meli.anuncios.repositories.AnuncioRepository;
import br.com.meli.anuncios.services.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AnuncioServiceImpl implements AnuncioService {
    @Autowired
    private AnuncioRepository repository;

    @Override
    public AnuncioDto create(AnuncioDto anuncioDto) {
        validationAnuncio( anuncioDto );

        Anuncio entity = new Anuncio();
        entity.setName(anuncioDto.getName());
        entity.setPrecoBase( anuncioDto.getPrecoBase() );
        entity.setPrecoPromocional( anuncioDto.getPrecoPromocional() );

        Anuncio savedEntity = repository.save(entity);

        AnuncioDto returnDto = new AnuncioDto();
        returnDto.setId(savedEntity.getId());
        returnDto.setName(savedEntity.getName());
        returnDto.setPrecoBase(savedEntity.getPrecoBase());
        returnDto.setPrecoPromocional(savedEntity.getPrecoPromocional());

        return returnDto;
    }


    @Override
    public AnuncioDto findById(Long id) {
        Anuncio entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anuncio nao encontrado"));

        AnuncioDto returnDto = new AnuncioDto(entity.getId(), entity.getName(), entity.getPrecoBase(), entity.getPrecoPromocional() );

        return returnDto;
    }

    @Override
    public List<AnuncioDto> findAll() {
        List<Anuncio> entities = repository.findAll();

        return entities.stream().map(e -> new AnuncioDto(e.getId(), e.getName(),e.getPrecoBase(),e.getPrecoPromocional())).collect(Collectors.toList());
    }

    @Override
    public AnuncioDto update(AnuncioDto anuncioDto) {
        validationAnuncio (anuncioDto);

        Anuncio entity = repository.findById(anuncioDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Anuncio nao encontrado"));

        entity.setName(anuncioDto.getName());
        entity.setPrecoBase(anuncioDto.getPrecoBase());
        entity.setPrecoPromocional(anuncioDto.getPrecoPromocional());

        entity = repository.save(entity);

        return new AnuncioDto(entity.getId(), entity.getName(), entity.getPrecoBase(), entity.getPrecoPromocional());
    }

    @Override
    public void delete(Long id) {
        Anuncio entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anuncio nao encontrado"));

        repository.delete(entity);
    }

    private void checkIsNameBlank(AnuncioDto anuncioDto){
                if (Objects.isNull( anuncioDto.getName() ) || anuncioDto.getName().isEmpty()) {
                    throw new BlankNameException();




                }
            }
    private void checkIsNameUnique(AnuncioDto anuncioDto) {
        //buscar no nd pelo nome
        //se existe retorna exception
        Optional<Anuncio> anuncioDB = repository.findByName( anuncioDto.getName() );
        if(anuncioDB.isPresent()){
            throw new UniqueViolationException();
        }

    }

    private static final List<String> invalidWords = Arrays.asList("magalu", "amazon", "americanas");

    private void checkInvalidName(AnuncioDto anuncioDto) {
        if (invalidWords.contains(anuncioDto.getName().toLowerCase())) {
            throw new InvalidWordException();
        }
    }
    private void ckeckIsValidPrice(AnuncioDto anuncioDto) {

        if(Objects.isNull(  anuncioDto.getPrecoBase()) || anuncioDto.getPrecoBase() <=0){
            throw new InvalidPriceException( "Preço base inválido" );
        }

        if (Objects.isNull(  anuncioDto.getPrecoPromocional()) || anuncioDto.getPrecoPromocional() <=0){
            throw new InvalidPriceException( "Preço promocional inválido" );

        }

       if (anuncioDto.getPrecoPromocional() > anuncioDto.getPrecoBase()) {
        throw new InvalidPromotionException( "Promoção inválido" );

        }

    }


    private void validationAnuncio(AnuncioDto anuncioDto) {
        checkIsNameBlank( anuncioDto );
        checkIsNameUnique( anuncioDto );
        ckeckIsValidPrice( anuncioDto );
        checkInvalidName( anuncioDto );

    }
}


