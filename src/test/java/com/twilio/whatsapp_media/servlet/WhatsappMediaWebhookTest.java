package com.twilio.whatsapp_media.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class WhatsappMediaWebhookTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void handleWebhookRequestWithImages() throws ServletException, IOException, URISyntaxException {

        var writer = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getParameter("NumMedia")).thenReturn("1");
        when(request.getContentType()).thenReturn("*/*");
        when(request.getCharacterEncoding()).thenReturn("UTF-8");

        var servlet = Mockito.spy(new WhatsappMediaWebhook());

        servlet.doPost(request, response);

        verify(response).setContentType("text/xml");
        var body = writer.toString();

        // assert
        assertEquals(body,
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<Response>" +
                "<Message>" +
                    "<Body>Thanks for the image! Here's one for you!</Body>" +
                    "<Media>https://images.unsplash.com/photo-1518717758536-85ae29035b6d?ixlib=rb-1.2.1&amp;ixid=eyJhcHBfaWQiOjEyMDd9&amp;auto=format&amp;fit=crop&amp;w=1350&amp;q=80</Media>" +
                "</Message>" +
            "</Response>");
    }

    @Test
    public void handleWebhookRequestWithoutImages() throws ServletException, IOException, URISyntaxException {

        var writer = new StringWriter();

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getParameter("NumMedia")).thenReturn("0");
        when(request.getContentType()).thenReturn("*/*");
        when(request.getCharacterEncoding()).thenReturn("UTF-8");

        var servlet = Mockito.spy(new WhatsappMediaWebhook());

        servlet.doPost(request, response);

        verify(response).setContentType("text/xml");
        var body = writer.toString();

        // assert
        assertEquals(body,
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<Response>" +
                "<Message>" +
                    "<Body>Send us an image!</Body>" +
                "</Message>" +
            "</Response>");
    }
}
