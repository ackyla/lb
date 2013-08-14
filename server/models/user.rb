require 'securerandom'

class User < ActiveRecord::Base
  before_create :generate_token
  belongs_to :room
  has_many :users
  has_many :locations
  has_many :hit_locations

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def enter_room(room)
    self.room = room
    self.save
  end

  def exit_room(room)
    self.room = nil
    self.save
  end

  def start_room
    if self.room != nil and self.id == self.room.owner.id
      room = self.room
      room.termination_time = DateTime.now.advance :minute => room.time_limit
      room.active = true
      room.save
    end
  end
end
