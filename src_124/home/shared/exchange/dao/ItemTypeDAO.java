/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package home.shared.exchange.dao;

import javax.xml.bind.JAXBElement;
import javax.xml.ws.Holder;

import com.microsoft.schemas.exchange.services._2006.messages.*;
import com.microsoft.schemas.exchange.services._2006.types.*;
import com.microsoft.schemas.exchange.services._2006.types.ObjectFactory;
import home.shared.exchange.util.ExchangeEnvironmentSettings;
import java.io.IOException;
import java.util.*;

/**
 * Data access object related to operations involving ItemType objects.
 *
 * @author Reid Miller
 */
public class ItemTypeDAO
{

    private ExchangeEnvironmentSettings exchangeEnvironmentSettings;

    public ItemTypeDAO( ExchangeEnvironmentSettings exchangeEnvironmentSettings )
    {
        this.exchangeEnvironmentSettings = exchangeEnvironmentSettings;
    }
    


    public List<ItemType> getFolderItems( ExchangeServicePortType port )
    {
        final DistinguishedFolderIdType distinguishedFolderIdType = new DistinguishedFolderIdType();
        distinguishedFolderIdType.setId(DistinguishedFolderIdNameType.INBOX);

        return getFolderItems( port, distinguishedFolderIdType );
    }

    public List<ItemType> getFolderItems( ExchangeServicePortType port, BaseFolderIdType folder_id )
    {

        /* We could build a NonEmptyArrayOfBaseFolderIdsType object to search
         * over multiple folders, but for now we will only add the Inbox
         * DistinguishedFolderIdType from above.
         */
        NonEmptyArrayOfBaseFolderIdsType nonEmptyArrayOfBaseFolderIdsType = new NonEmptyArrayOfBaseFolderIdsType();
        nonEmptyArrayOfBaseFolderIdsType.getFolderIdOrDistinguishedFolderId().add(folder_id);

        /* Tell Exchange you want the response objects to contain all the
         * ItemType properties available to a findItem request. See limitations
         * of findItem requests at http://msdn.microsoft.com/en-us/library/bb508824.aspx
         */
        final ItemResponseShapeType itemResponseShapeType = new ItemResponseShapeType();
        itemResponseShapeType.setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);

        /* Now make use of both the objects from above to construct a minimal
         * FindItemType request to be sent.
         */
        FindItemType request = new FindItemType();
        request.setTraversal(ItemQueryTraversalType.SHALLOW); // SHALLOW means it doesn't look for "soft deleted" items.
        request.setItemShape(itemResponseShapeType);
        request.setParentFolderIds(nonEmptyArrayOfBaseFolderIdsType);

        /* Create a FindItemResponseType to be sent with the SOAP request. The
         * Holder is filled with the response from Exchange and sent back through
         * the SOAP response.
         */
        FindItemResponseType findItemResponse = new FindItemResponseType();


        Holder<FindItemResponseType> findItemResult = new Holder<FindItemResponseType>(findItemResponse);

        /* Make SOAP request for ItemTypes to EWS */

        port.findItem(request, exchangeEnvironmentSettings.getMailboxCulture(), exchangeEnvironmentSettings.getRequestServerVersion(), findItemResult, exchangeEnvironmentSettings.getServerVersionInfoHolder());

        /* Create List of ItemTypes to be returned by the method */
        List<ItemType> items = new ArrayList<ItemType>();

        /* Evaluate the FindItemResponseType sent back by Exchange */
        FindItemResponseType response = (FindItemResponseType) findItemResult.value;
        ArrayOfResponseMessagesType arrayOfResponseMessagesType = response.getResponseMessages();
        List responseMessageTypeList = arrayOfResponseMessagesType.getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage(); // Note: Best method name... ever!

        /* Response is a List of JAXBElement objects. Iterate through them */
        Iterator responseMessagesIterator = responseMessageTypeList.iterator();

        /* In this basic example, there's only one response message
         * (JAXBElement) from Exchange.
         */
        while (responseMessagesIterator.hasNext())
        {
            JAXBElement jaxBElement = (JAXBElement) responseMessagesIterator.next();
            FindItemResponseMessageType findItemResponseMessageType = (FindItemResponseMessageType) jaxBElement.getValue();
            FindItemParentType findItemParentType = findItemResponseMessageType.getRootFolder();

            /* 
             * If it is null (empty) then the folder does not contain any elements
             */
            if (findItemParentType != null)
            {
                /* Our one response message contained a FindItemParentType with
                 * an ArrayOfRealItemsType (an array of ItemTypes)
                 */
                ArrayOfRealItemsType arrayOfRealItemsType = findItemParentType.getItems();
                List itemList = arrayOfRealItemsType.getItemOrMessageOrCalendarItem();

                /* Iterate through the List of ItemTypes. */
                Iterator itemListIter = itemList.iterator();
                while (itemListIter.hasNext())
                {
                    ItemType itemType = (ItemType) itemListIter.next();
                    items.add(itemType);
                }
            }
            else
            {
                /* TODO: Handle failed requests and empty responses. I'm
                 * glossing over this for now.
                 */
            }
        }

        return items;
    }
   
    public static String EXCHANGE_MAIL_FOLDER_CLASS = "IPF.Note";

    public List<BaseFolderType> GetFolders( ExchangeServicePortType port ) throws IOException
    {
        DistinguishedFolderIdType msgFolderRoot = new DistinguishedFolderIdType();
        msgFolderRoot.setId(DistinguishedFolderIdNameType.MSGFOLDERROOT);

        return GetFolders(  port, msgFolderRoot );
    }

    public List<BaseFolderType> GetFolders( ExchangeServicePortType port, BaseFolderIdType parent_folder_id ) throws IOException
    {
        List<BaseFolderType> mailFolders = new ArrayList<BaseFolderType>();

        ObjectFactory of = new ObjectFactory();
        FindFolderType findRequest = new FindFolderType();
        findRequest.setTraversal(FolderQueryTraversalType.SHALLOW);
        findRequest.setFolderShape(new FolderResponseShapeType());
        findRequest.getFolderShape().setBaseShape(DefaultShapeNamesType.ALL_PROPERTIES);
        // we also want to grab the managed folder information
        //
        PathToUnindexedFieldType managedFolderProp = new PathToUnindexedFieldType();
        managedFolderProp.setFieldURI(UnindexedFieldURIType.FOLDER_MANAGED_FOLDER_INFORMATION);

        PathToUnindexedFieldType displayNameFolderProp = new PathToUnindexedFieldType();
        displayNameFolderProp.setFieldURI(UnindexedFieldURIType.FOLDER_DISPLAY_NAME);

        NonEmptyArrayOfPathsToElementType value = new NonEmptyArrayOfPathsToElementType();
        value.getPath().add(of.createFieldURI(managedFolderProp));
        value.getPath().add(of.createFieldURI(displayNameFolderProp));
        findRequest.getFolderShape().setAdditionalProperties(value);

        NonEmptyArrayOfBaseFolderIdsType neabft = new NonEmptyArrayOfBaseFolderIdsType();
        neabft.getFolderIdOrDistinguishedFolderId().add(parent_folder_id);
        findRequest.setParentFolderIds(neabft);

        // make the call and parse the response
        //
        //FindFolderResponseType response = port.findFolder(findRequest, null, null, null, null, null, null);

        /* Create a FindItemResponseType to be sent with the SOAP request. The
         * Holder is filled with the response from Exchange and sent back through
         * the SOAP response.
         */
        FindFolderResponseType findItemRType = new FindFolderResponseType();


        Holder<FindFolderResponseType> findFolderResult = new Holder<FindFolderResponseType>(findItemRType);

        /* Make SOAP request for ItemTypes to EWS */

        port.findFolder(findRequest, exchangeEnvironmentSettings.getMailboxCulture(),
                exchangeEnvironmentSettings.getRequestServerVersion(),
                findFolderResult, exchangeEnvironmentSettings.getServerVersionInfoHolder());

        /* Create List of ItemTypes to be returned by the method */
        List<ItemType> items = new ArrayList<ItemType>();

        /* Evaluate the FindItemResponseType sent back by Exchange */
        FindFolderResponseType response = (FindFolderResponseType) findFolderResult.value;
        ArrayOfResponseMessagesType arrayOfResponseMessagesType = response.getResponseMessages();
        List responseMessageTypeList = arrayOfResponseMessagesType.getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage(); // Note: Best method name... ever!

        /* Response is a List of JAXBElement objects. Iterate through them */
        Iterator responseMessagesIterator = responseMessageTypeList.iterator();

        /* In this basic example, there's only one response message
         * (JAXBElement) from Exchange.
         */
        while (responseMessagesIterator.hasNext())
        {
            JAXBElement jaxResponse = (JAXBElement) responseMessagesIterator.next();

            ResponseMessageType rr = (ResponseMessageType) jaxResponse.getValue();
            if (rr.getResponseClass().equals(ResponseClassType.ERROR))
            {
                throw new IOException("Get Child Folders Response Error: " + rr.getMessageText());
            }
            else if (rr.getResponseClass().equals(ResponseClassType.WARNING))
            {
                throw new IOException("Get Child Folders Response Warning: " + rr.getMessageText());
            }
            else if (rr.getResponseClass().equals(ResponseClassType.SUCCESS))
            {
                FindFolderResponseMessageType findResponse = (FindFolderResponseMessageType) rr;
                List<BaseFolderType> allFolders = findResponse.getRootFolder().getFolders().getFolderOrCalendarFolderOrContactsFolder();
                for (BaseFolderType folder : allFolders)
                {
                    if (folder.getFolderClass() == null
                            || folder.getFolderClass().equals(EXCHANGE_MAIL_FOLDER_CLASS))
                    {
                        mailFolders.add(folder);
                    }
                }
            }
        }
        return mailFolders;
    }
}
