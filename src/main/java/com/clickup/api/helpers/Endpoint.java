package com.clickup.api.helpers;

public class Endpoint {

    public String createSpace(String teamId){
        return String.format("team/%s/space", teamId);
    }

    public String getSpace(String spaceId){
        return String.format("space/%s", spaceId);
    }

    public String getSpaces(String teamId){
        return String.format("team/%s/space", teamId);
    }

    public String updateSpace(String spaceId) {
        return String.format("space/%s", spaceId);
    }

    public String deleteSpace(String spaceId) {
        return String.format("space/%s", spaceId);
    }

    public String createFolder(String spaceId){
        return String.format("space/%s/folder", spaceId);
    }

    public String getFolder(String folderId){
        return  String.format("folder/%s", folderId);
    }

    public String getFolders(String spaceId){
        return String.format("space/%s/folder", spaceId);
    }

    public String updateFolder(String folderId) {
        return String.format("folder/%s", folderId);
    }

    public String deleteFolder(String folderId) {
        return String.format("folder/%s", folderId);
    }

    public String createList(String folderId){
        return String.format("folder/%s/list", folderId);
    }

    public String getList(String listId){
        return String.format("list/%s", listId);
    }

    public String getLists(String folderId){
        return String.format("folder/%s/list", folderId);
    }

    public String updateList(String listId){
        return String.format("list/%s", listId);
    }

    public String deleteList(String listId){
        return String.format("list/%s", listId);
    }

    public String createListFolderless(String spaceId){
        return String.format("space/%s/list", spaceId);
    }

    public String getListFolderless(String spaceId){
        return String.format("space/%s/list", spaceId);
    }

    public String getTeam(){
        return "team";
    }

}
