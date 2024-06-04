package com.chatex.chatapp.controller;

import com.chatex.chatapp.vo.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {

    static int roomNumber = 0;
    // 방의 정보를 담아둘 List<Room>컬랙션을 생성
    List<Room> roomList = new ArrayList<Room>();

    @RequestMapping("/chat")
    public ModelAndView chat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("chat");

        return mv;
    }

    // 방 페이지
    @RequestMapping("/room")
    public ModelAndView room() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("room");

        return mv;
    }

    // 방생성하기
    @RequestMapping("/createRoom")
    public @ResponseBody List<Room> createRoom(@RequestParam HashMap<Object, Object> params) {
        String roomName = (String) params.get("roomName");

        if (roomName != null && !roomName.trim().equals("")) {
            Room room = new Room();
            room.setRoomNumber(++roomNumber);
            room.setRoomName(roomName);
            roomList.add(room);
        }

        return roomList;
    }

    // 방 정보 가져오기
    @RequestMapping("/getRoom")
    public @ResponseBody List<Room> getRoom(@RequestParam HashMap<Object, Object> params) {
        return roomList;
    }

    // 채팅방
    @RequestMapping("/moveChating")
    public ModelAndView chating(@RequestParam HashMap<Object, Object> params) {
        ModelAndView mv = new ModelAndView();
        int roomNumber = Integer.parseInt((String) params.get("roomNumber"));

        // roomList 내의 모든 Room 객체들 중에서
        // roomNumber와 일치하는 방 번호를 가진 객체들만을 선택하여 새로운 리스트로 만듦
        List<Room> resultRoom = roomList.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .collect(Collectors.toList());

        if (resultRoom != null && resultRoom.size() > 0) {
            mv.addObject("roomName", params.get("roomName"));
            mv.addObject("roomNumber", params.get("roomNumber"));
            mv.setViewName("chat");
        }else {
            mv.setViewName("room");
        }

        return mv;
    }

}
