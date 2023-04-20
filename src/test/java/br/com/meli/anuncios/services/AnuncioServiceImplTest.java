package br.com.meli.anuncios.services;

import br.com.meli.anuncios.dto.AnuncioDto;
import br.com.meli.anuncios.entitites.Anuncio;
import br.com.meli.anuncios.exceptions.ResourceNotFoundException;
import br.com.meli.anuncios.repositories.AnuncioRepository;
import br.com.meli.anuncios.services.impl.AnuncioServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;



@DisplayName("Anuncio service")
@ExtendWith(MockitoExtension.class)
public class AnuncioServiceImplTest {
    @InjectMocks
    private AnuncioServiceImpl anuncioService;
    @Mock
    private AnuncioRepository anuncioRepository;

    @Test
    @DisplayName("Quando create for chamado, deverá retornar um anuncioDto valido")
    void createTest() {
        AnuncioDto anuncioDto = new AnuncioDto( 1L, "name", 2.00, 1.00 );
        Anuncio anuncio = new Anuncio();
        anuncio.setName( "name" );
        anuncio.setPrecoBase( 2.00 );
        anuncio.setPrecoPromocional( 1.00 );

        when( anuncioRepository.save( anuncio ) ).thenReturn( anuncio );

        AnuncioDto result = anuncioService.create( anuncioDto );

        assertEquals( result.getName(), anuncioDto.getName() );
        verify( anuncioRepository, times( 1 ) ).save( anuncio );
    }

    @Test
    @DisplayName("Quando findById for chamado, deverá retornar um anuncioDto valido")
    void findByIdTest() {
        Anuncio anuncioEntity = new Anuncio();
        anuncioEntity.setId( 1L );
        anuncioEntity.setName( "name" );
        anuncioEntity.setPrecoBase( 2.0 );
        anuncioEntity.setPrecoPromocional( 1.0 );

        when( anuncioRepository.findById( 1L ) ).thenReturn( Optional.of( anuncioEntity ) );
        AnuncioDto resultDto = anuncioService.findById( 1L );
        assertEquals( resultDto.getId(), 1L );
        verify( anuncioRepository, times( 1 ) ).findById( 1L );
    }

    @Test
    @DisplayName("Quando findById for chamado, deverá retornar um error")
    void findByIdErrorTest() {
        when( anuncioRepository.findById( 1L ) ).thenReturn( Optional.empty() );
        Assertions.assertThrows( ResourceNotFoundException.class, () -> anuncioService.findById( 1L ) );
    }

    @Test
    @DisplayName("Quando update for chamado, deverá retornar um anuncioDto modificado")
    public void testUpdate() {
        // Define anúncio original
        AnuncioDto anuncioDto = new AnuncioDto(1L, "Anuncio 1", 10.0, 5.0);

        // Define anúncio modificado
        AnuncioDto anuncioModificado = new AnuncioDto(1L, "Anuncio 1 Modificado", 15.0, 7.5);

        // Define comportamento esperado do mock do repositório
        when(anuncioRepository.findById(anuncioDto.getId()))
                .thenReturn(Optional.ofNullable(AnuncioMapper.toAnuncio(anuncioDto)));

        // Verifica se o resultado retornado pelo método update é igual ao anúncio modificado
        assertEquals(anuncioModificado, resultado);
    }


    @Test
    @DisplayName("Quando update for chamado, deverá retornar um anuncioDto modificado")
    public void testFindAll() {

        AnuncioDto anuncio1 = new AnuncioDto(1L, "Anuncio 1", 10.0, 5.0);
        AnuncioDto anuncio2 = new AnuncioDto(2L, "Anuncio 2", 20.0, 15.0);
        List<AnuncioDto> entities = Arrays.asList(anuncio1, anuncio2);

        when(anuncioRepository.findAll())
                .thenReturn(entities.stream().map(e -> new Anuncio(
                        e.getId(), e.getName(), e.getPrecoBase(), e.getPrecoPromocional())).collect(Collectors.toList()));

        List<AnuncioDto> entities2 = anuncioService.findAll( );



    }
}




