class AddOwnerToRoom < ActiveRecord::Migration
  def self.up
    change_table :rooms do |t|
      t.references :owener
    end
  end

  def self.down
    change_table :rooms do |t|
      t.remove :owener
    end
  end
end
