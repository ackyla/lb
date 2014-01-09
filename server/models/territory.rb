# -*- coding: utf-8 -*-
class Territory < ActiveRecord::Base
  belongs_to :owner, class_name: "User"
  belongs_to :character
  has_and_belongs_to_many :locations
  has_many :detections
  has_many :invasions
  has_many :invaders, class_name: "User", through: :invasions, source: :user
  scope :actives, -> { where("expiration_date >= ? ", Time.now) }

  before_create do
    self.radius = character.radius
    self.precision = character.precision
    # デフォルトの有効期限は1日
    self.expiration_date = DateTime.now + 1
  end

  def detect(user, loc)
    invaders << user
    det = detections.new(location: loc)
    self.detection_count += 1
    owner.add_exp 1
    Notification.create(notification_type: "entering", user: user, detection: det)
    Notification.create(notification_type: "detection", user: self.owner, detection: det)
    self.save
  end

  def add_location(loc)
    return unless loc.distance(self) < self.radius
    locations << loc
    self.save
  end

  def to_hash
    hash = Hash[self.attributes]
    hash[:territory_id] = self.id
    hash.delete "id"
    hash
  end

  def supply(point)
    return unless owner.use_point point
    self.expiration_date += (point * 10).hours
    save
  end

  def within_range(loc)
    loc.distance(self) < self.character.distance
  end
end
