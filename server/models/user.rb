require 'securerandom'

class User < ActiveRecord::Base
  belongs_to :room
  before_create :generate_token
  has_many :users
  has_many :locations

  def generate_token
    token = SecureRandom.base64(16)
    self.token = token
  end

  def enter_room(room)
    self.room = room
    self.save
  end

  def current_locations
    return nil unless self.room
    self.locations.where(:room_id => self.room.id).order('created_at desc').limit(30)
  end
end
